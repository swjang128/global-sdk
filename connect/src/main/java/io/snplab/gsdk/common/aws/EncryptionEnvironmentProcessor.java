package io.snplab.gsdk.common.aws;

import io.snplab.gsdk.common.AESCrypto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import javax.xml.bind.DatatypeConverter;
import java.util.Map;
import java.util.Properties;

public class EncryptionEnvironmentProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        String mode = System.getenv("OPERATION_MODE");
        if ("dev".equals(mode)) return;

        Map<String, Object> map = environment.getSystemEnvironment();

        String mailHost = (String) map.get("MAIL_HOST");
        String mailUser = (String) map.get("MAIL_USERNAME");
        String mailPassword = (String) map.get("MAIL_PASSWORD");
        String mysqlUrl = (String) map.get("MYSQL_SERVER_URL");
        String mysqlUser = (String) map.get("MYSQL_USER");
        String mysqlPwd = (String) map.get("MYSQL_PASSWORD");
        String cipherPropertyKey = (String) map.get("SERVER_PROPERTY_KEY");

        KMSCrypto crypto = new KMSCrypto();
        String propertyKey = crypto.decrypt(cipherPropertyKey);

        byte[] secretKey = DatatypeConverter.parseHexBinary(propertyKey.substring(0, 64));
        byte[] iv = DatatypeConverter.parseHexBinary(propertyKey.substring(64));

        AESCrypto aesCrypto = new AESCrypto();
        aesCrypto.setSecretKey(secretKey);

        Properties properties = new Properties();
        properties.put("spring.datasource.url", aesCrypto.decryptCBC(mysqlUrl.getBytes(), iv));
        properties.put("spring.datasource.username", aesCrypto.decryptCBC(mysqlUser.getBytes(), iv));
        properties.put("spring.datasource.password", aesCrypto.decryptCBC(mysqlPwd.getBytes(), iv));
        properties.put("spring.mail.host", aesCrypto.decryptCBC(mailHost.getBytes(), iv));
        properties.put("spring.mail.username", aesCrypto.decryptCBC(mailUser.getBytes(), iv));
        properties.put("spring.mail.password", aesCrypto.decryptCBC(mailPassword.getBytes(), iv));

        environment.getPropertySources().addFirst(new PropertiesPropertySource("myProp", properties));
    }
}
