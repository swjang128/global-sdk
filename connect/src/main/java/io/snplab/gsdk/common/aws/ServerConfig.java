package io.snplab.gsdk.common.aws;

import io.snplab.gsdk.common.util.AESCrypto;
import io.snplab.gsdk.common.domain.OperationMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component("ServerConfig")
public class ServerConfig {

    private final static int KEY_SIZE = 64;

    protected static Map<String, String> environments = new ConcurrentHashMap<>();

    private static boolean isTestMode = false;



    public static String get(EnvironmentKey key) {
        return environments.get(key.name());
    }

    @PostConstruct
    private void init() {

        log.info("*** server config init ***");

        Map<String, String> map = System.getenv();
        EnvironmentKey[] keys = EnvironmentKey.values();

        OperationMode mode = (isTestMode ? OperationMode.dev : OperationMode.valueOf(map.get("OPERATION_MODE")));
        if (mode.equals(OperationMode.dev)) {
            for (EnvironmentKey key : keys) {
                if (key.equals(EnvironmentKey.SERVER_PROPERTY_KEY))
                    continue;
                String keyName = key.name();
                if (map.containsKey(keyName)) {
                    environments.put(keyName, map.get(keyName));
                }
            }
            return;
        }

        String cipherPrivateKey = map.get(EnvironmentKey.SERVER_PRIVATE_KEY.name());
        String cipherPropertyKey = map.get(EnvironmentKey.SERVER_PROPERTY_KEY.name());

        KMSCrypto crypto = new KMSCrypto();
        String privateKey = crypto.decrypt(cipherPrivateKey);
        String propertyKey = crypto.decrypt(cipherPropertyKey);

        environments.put(EnvironmentKey.SERVER_PRIVATE_KEY.name(), privateKey);

        byte[] secretKey = DatatypeConverter.parseHexBinary(propertyKey.substring(0, KEY_SIZE));
        byte[] iv = DatatypeConverter.parseHexBinary(propertyKey.substring(KEY_SIZE));

        AESCrypto aesCrypto = new AESCrypto();
        aesCrypto.setSecretKey(secretKey);

        for (EnvironmentKey key : keys) {
            if (key.equals(EnvironmentKey.SERVER_PRIVATE_KEY)
                    || key.equals(EnvironmentKey.SERVER_PROPERTY_KEY))
                continue;

            String keyName = key.name();
            String cipherText = map.get(keyName);
            String plainText = null;
            if (cipherText == null) {
                log.error("ServerConfig: Value in environment is missing " + keyName);
            } else {
                try {
                    plainText = aesCrypto.decryptCBC(cipherText.getBytes(), iv);
                } catch (IllegalArgumentException e) {
                    log.error("ServerConfig: base64 decoding failed for " + keyName);
                }

                if (plainText == null) {
                    log.error("ServerConfig: decryption failed for " + keyName);
                }
            }
            if (plainText != null) {
                environments.put(keyName, plainText);
            }

        }
    }
}
