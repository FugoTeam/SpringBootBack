package com.fugoworld.backend.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class PasswordService {
    private static final String FUGO_WORLD_PASSWORD = "FugoWorldPassword";
    private static final byte[] salt = "12345678".getBytes();
    private static final int iterationCount = 40000;
    private static final int keyLength = 128;

    private static SecretKeySpec createSecretKey(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, PasswordService.salt, PasswordService.iterationCount, PasswordService.keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    public static String encrypt(String password) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = PasswordService.createSecretKey(FUGO_WORLD_PASSWORD.toCharArray());
        pbeCipher.init(1, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
        byte[] iv = ivParameterSpec.getIV();
        return PasswordService.base64Encode(iv) + ":" + PasswordService.base64Encode(cryptoText);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(String pwd) throws GeneralSecurityException, IOException {
        SecretKeySpec key = PasswordService.createSecretKey(FUGO_WORLD_PASSWORD.toCharArray());
        String iv = pwd.split(":")[0];
        String property = pwd.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(2, key, new IvParameterSpec(PasswordService.base64Decode(iv)));
        return new String(pbeCipher.doFinal(PasswordService.base64Decode(property)), StandardCharsets.UTF_8);
    }

    public static boolean check(String pwdInDb, String pwdFromForm){
        try {
            return pwdFromForm.equals(PasswordService.decrypt(pwdInDb));
        }
        catch (IOException | GeneralSecurityException e) {
            return false;
        }
    }

    private static byte[] base64Decode(String property) {
        return Base64.getDecoder().decode(property);
    }

    public static boolean isSecurePassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        if (!hasUppercase || !hasLowercase || !hasDigit) {
            return false;
        }

        String specialCharacters = "!@#$%^&*()-+";
        boolean hasSpecialCharacter = false;
        for (char c : password.toCharArray()) {
            if (specialCharacters.contains(String.valueOf(c))) {
                hasSpecialCharacter = true;
                break;
            }
        }

        return hasSpecialCharacter;
    }
}
