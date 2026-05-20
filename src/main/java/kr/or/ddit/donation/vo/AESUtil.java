package kr.or.ddit.donation.vo;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {

	// 16자리 고정 키 
    private static final String KEY = "AnimalShelter123";

    public static String encrypt(String text) throws Exception {
        if (text == null || text.isEmpty()) return text;
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes("UTF-8")));
    }

    public static String decrypt(String encrypted) throws Exception {
        if (encrypted == null || encrypted.isEmpty()) return encrypted;
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)), "UTF-8");
    }
}
