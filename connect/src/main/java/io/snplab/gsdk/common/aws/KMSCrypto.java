package io.snplab.gsdk.common.aws;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec;

import java.nio.ByteBuffer;
import java.util.Base64;

public class KMSCrypto {

    private static final String KEY_ID = "alias/server-secrets-protection-key-pair";

    private static AWSKMS client;

    static {
        client = AWSKMSClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();
    }

    public String decrypt(String cipherText) {

        DecryptRequest request = new DecryptRequest()
                .withKeyId(KEY_ID)
                .withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)
                .withCiphertextBlob(ByteBuffer.wrap(Base64.getDecoder().decode(cipherText)));

        DecryptResult response = client.decrypt(request);
        ByteBuffer plainText = response.getPlaintext();
        return new String(plainText.array());
    }
}
