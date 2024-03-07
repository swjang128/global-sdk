package io.snplab.gsdk.common;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESCrypto {

    private SecretKeySpec symmetricKey;
    private SecretKeySpec secretKey; //for config

    private final static String AES_ALGORITHM_SPEC = "AES/CBC/PKCS5Padding";
    final static String SYMMETRIC_CIPHER_TRANSFORMATION = "AES/GCM/NoPadding";
    final static String SYMMETRIC_KEY_ALGORITHM = "AES";
    final static int GCM_TAG_SIZE_BITS = 128; //bits
    public final static int GCM_TAG_SIZE_IN_BYTES = 16;

    public boolean setSecretKeyData(byte[] sharedSecretKeyData) {
        if (sharedSecretKeyData != null) {
            try {
                symmetricKey = new SecretKeySpec(sharedSecretKeyData, SYMMETRIC_KEY_ALGORITHM);

                return true;
            } catch (Exception e) {
                System.err.println("Fail to get secret key spec: "+e);
            }
        }
        return false;
    }

    public byte[] encryptData(byte[] data, byte[] ivData) {
        if (symmetricKey == null) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(SYMMETRIC_CIPHER_TRANSFORMATION);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_SIZE_BITS, ivData);

            cipher.init(Cipher.ENCRYPT_MODE, symmetricKey, gcmParameterSpec);

            return cipher.doFinal(data);

        } catch (NoSuchAlgorithmException |
                 IllegalBlockSizeException |
                 BadPaddingException |
                 InvalidKeyException |
                 InvalidAlgorithmParameterException |
                 NoSuchPaddingException e) {
            System.err.println("Fail to encrypt attestation result " + e);
            return null;
        }
    }

    public boolean setSecretKey(byte[] secretKeyData) {

        if (secretKeyData != null) {
            try {
                secretKey = new SecretKeySpec(secretKeyData, SYMMETRIC_KEY_ALGORITHM);

                return true;
            } catch (Exception e) {
                System.err.println("Fail to get secret key spec: "+e);
            }
        }
        return false;
    }

    public byte[] encryptCBC(byte[] data, byte[] ivData) {
        if (symmetricKey == null) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM_SPEC);
            cipher.init(Cipher.ENCRYPT_MODE, symmetricKey, new IvParameterSpec(ivData));

            return cipher.doFinal(data);

        } catch (NoSuchAlgorithmException |
                 IllegalBlockSizeException |
                 BadPaddingException |
                 InvalidKeyException |
                 InvalidAlgorithmParameterException |
                 NoSuchPaddingException e) {
            System.err.println("Fail to encrypt attestation result " + e);
            return null;
        }
    }

    public String decryptCBC(byte[] encryptedData, byte[] iv) {

        if (secretKey == null) {
            System.out.println("secretKey is null!");
            return null;
        }

        byte[] decodedEncryptData = Base64.getDecoder().decode(encryptedData);

        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM_SPEC);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            return new String(cipher.doFinal(decodedEncryptData));
        } catch (NoSuchAlgorithmException |
                 NoSuchPaddingException |
                 InvalidKeyException |
                 InvalidAlgorithmParameterException |
                 IllegalBlockSizeException |
                 BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
