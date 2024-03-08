package io.snplab.gsdk.common.util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AES256 {

    private final byte[] ivBytes = {30, -4, -33, -97, 43, -74, -72, -1, 39, 24, -46, -9, 12, -47, -79, 30};

    public String encrypt(String data) {

        // AES-256-CBC 방식 암호화
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("NoSuchAlgorithmException or NoSuchPaddingException");
        }

        SecretKey secretKey = new SecretKeySpec(KeyUtil.B2B_CRYPTO_KEY.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new RuntimeException("InvalidKeyException");
        }

        byte[] dataBytes = null;
        try {
            dataBytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("UnsupportedEncodingException");
        }

        byte[] encryptedData = null;
        try {
            encryptedData = cipher.doFinal(dataBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("IllegalBlockSizeException or BadPaddingException");
        }

        String base64EncryptedData = Base64.getUrlEncoder().encodeToString(encryptedData);

        return base64EncryptedData;
    }

    public String decrypt(String base64EncryptedData) {

        // AES-256-CBC 방식 복호화
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("NoSuchAlgorithmException or NoSuchPaddingException");
        }

        SecretKey secretKey = new SecretKeySpec(KeyUtil.B2B_CRYPTO_KEY.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new RuntimeException("InvalidKeyException");
        }

        byte[] encryptedData = Base64.getUrlDecoder().decode(base64EncryptedData);
        byte[] data = null;
        try {
            data = cipher.doFinal(encryptedData);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("IllegalBlockSizeException or BadPaddingException");
        }

        return new String(data);
    }
}
