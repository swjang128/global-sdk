package io.snplab.gsdk.common.service;

import io.snplab.gsdk.common.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
public class AES256 {
    private final Cipher cipher;
    private final IvParameterSpec iv;

    public AES256() {
        // AES-256-CBC 방식
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.error("aes256 encrypt config error: {}", e.getMessage());
            throw new RuntimeException("NoSuchAlgorithmException or NoSuchPaddingException");
        }
        byte[] ivBytes = {30, -4, -33, -97, 43, -74, -72, -1, 39, 24, -46, -9, 12, -47, -79, 30};
        this.iv = new IvParameterSpec(ivBytes);
    }

    public String encrypt(String data) {
        SecretKey secretKey = new SecretKeySpec(KeyUtil.B2B_CRYPTO_KEY.getBytes(), "AES");
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new RuntimeException("InvalidKeyException");
        }

        byte[] dataBytes = null;
        dataBytes = data.getBytes(StandardCharsets.UTF_8);

        byte[] encryptedData = null;
        try {
            encryptedData = cipher.doFinal(dataBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("IllegalBlockSizeException or BadPaddingException");
        }

        return Base64.getUrlEncoder().encodeToString(encryptedData);
    }

    public String decrypt(String base64EncryptedData) {
        SecretKey secretKey = new SecretKeySpec(KeyUtil.B2B_CRYPTO_KEY.getBytes(), "AES");
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
