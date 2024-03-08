package com.parserlabs.phr.utils;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Suraj
 *
 */
@Slf4j
@UtilityClass
public class PhrAuthTsUtility {

	private static final String ALGO = "AES"; // Default uses ECB PKCS5Padding

	public String encrypt(String data, String secret) throws Exception {
		Key key = generateKey(secret);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encVal);

	}

	public static String decrypt(String strToDecrypt, String secret) {
		try {
			Key key = generateKey(secret);
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			log.info("Error while decrypting: " + e.toString());
		}
		return null;
	}

	private Key generateKey(String secret) {
		byte[] decoded = Base64.getDecoder().decode(secret.getBytes());
		return new SecretKeySpec(decoded, ALGO);
	}

	public String decodeKey(String str) {
		byte[] decoded = Base64.getDecoder().decode(str.getBytes());
		return new String(decoded);
	}

	public String encodeKey(String str) {
		byte[] encoded = Base64.getEncoder().encode(str.getBytes());
		return new String(encoded);
	}

	public static void main(String a[]) throws Exception {
		/*
		 * Secret Key must be in the form of 16 byte like,
		 *
		 * private static final byte[] secretKey = new byte[] { ‘m’, ‘u’, ‘s’, ‘t’, ‘b’,
		 * ‘e’, ‘1’, ‘6’, ‘b’, ‘y’, ‘t’,’e’, ‘s’, ‘k’, ‘e’, ‘y’};
		 *
		 * below is the direct 16byte string we can use
		 */
		String secretKey = "hP2@dEVkE#sECERT";
		String encodedBase64Key = encodeKey(secretKey);
		System.out.println("EncodedBase64Key = " + encodedBase64Key); // This need to be share between client and server

		// To check actual key from encoded base 64 secretKey
		// String toDecodeBase64Key = decodeKey(encodedBase64Key);

		String toEncrypt = "Please encrypt this message!";
		System.out.println("Plain text = " + toEncrypt);

		// AES Encryption based on above secretKey
		String encrStr = encrypt(toEncrypt, encodedBase64Key);
		System.out.println("Cipher Text: Encryption of str = " + encrStr);

		// AES Decryption based on above secretKey
		String decrStr = decrypt(encrStr, encodedBase64Key);
		System.out.println("Decryption of str = " + decrStr);
	}

}
