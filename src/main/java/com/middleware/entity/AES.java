package com.middleware.entity;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AES{

//	private static SecretKeySpec secretKey;
//	private static byte[] key;
//
//	public static String generateSessionToken(String email, String code, String secretKey) {
//		return encrypt(email + code, secretKey);
//	}
//
//	public static String hex(byte[] bytes) {
//		char[] result = Hex.encodeHex(bytes);
//		return new String(result);
//	}
//
//	public static void setKey(String myKey) {
//		MessageDigest sha = null;
//		try {
//			key = myKey.getBytes("UTF-8");
//			sha = MessageDigest.getInstance("SHA-1");
//			key = sha.digest(key);
//			key = Arrays.copyOf(key, 16);
//			secretKey = new SecretKeySpec(key, "AES");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static String encrypt(String strToEncrypt, String secret) {
//		try {
//			setKey(secret);
//			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//			return hex(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
//		} catch (Exception e) {
//			System.out.println("Error while encrypting: " + e.toString());
//		}
//		return null;
//	}

//	public static String decrypt(String strToDecrypt, String secret) {
//		try {
//			setKey(secret);
//			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
//			cipher.init(Cipher.DECRYPT_MODE, secretKey);
//			return new String(cipher.doFinal(Hex.decodeHex(strToDecrypt.toCharArray())));
//		} catch (Exception e) {
//			System.out.println("Error while decrypting: " + e.toString());
//		}
//		return null;
//	}
	
	
	static String PLAIN_TEXT = "SS18617710213463"; 
    static String ENCRYPTION_KEY = "mykey@91mykey@91";
    static String INITIALIZATIO_VECTOR = "AODVNUASDNVVAOVF";

    public static void main(String [] args) {
        try {

            System.out.println("Plain text: " + PLAIN_TEXT);

            byte[] encryptedMsg = encrypt(PLAIN_TEXT, ENCRYPTION_KEY);
            String base64Encrypted = Base64.getEncoder().encodeToString(encryptedMsg);
            System.out.println("Encrypted: "+  base64Encrypted);

            byte[] base64Decrypted = Base64.getDecoder().decode(base64Encrypted);
            String decryptedMsg = decrypt(base64Decrypted, ENCRYPTION_KEY);
            System.out.println("Decrypted: " + decryptedMsg);
        } catch (Exception e) { 
            e.printStackTrace();
        } 
    }

    public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/pkcs5padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(INITIALIZATIO_VECTOR.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
      }

      public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/pkcs5padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(INITIALIZATIO_VECTOR.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText),"UTF-8");
      }

}
