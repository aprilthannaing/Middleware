package com.middleware.service.impl;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.Hex;

public class CommonService {
	public static String generateSession(String value) {
		//String value = json.get("session").toString();
		char[] chars = value.toCharArray();
		String key = "S1S2S3";
		int iterations = 500;
		byte[] salt = key.getBytes();
		String hashPass = "";
		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations,64*2);
		SecretKeyFactory skf;
		try {
			skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = skf.generateSecret(spec).getEncoded();
			hashPass = Hex.encodeHexString(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return hashPass;
}
}
