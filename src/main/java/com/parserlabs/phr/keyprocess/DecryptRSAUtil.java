package com.parserlabs.phr.keyprocess;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.config.security.JwtTokenUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DecryptRSAUtil {

	public static PublicKey getPublicKey(String base64PublicKey) {
		PublicKey publicKey = null;
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
			log.error("No such Alogorithm exception occured during getting public key:", e);
		} catch (InvalidKeySpecException e) {
			log.error("Invalid key exception occured during getting public key:", e);
		}
		return publicKey;
	}

	/**
	 * Get the Private key
	 * @param base64PrivateKey
	 * @return
	 */
	public static PrivateKey getPrivateKey(String base64PrivateKey) {
		PrivateKey privateKey = null;
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			log.error("No such Alogorithm exception occured during getting private key:", e);
		}
		try {
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (InvalidKeySpecException e) {
			log.error("Invalid key exception occured during getting private key:", e);
		}
		return privateKey;
	}

	public static byte[] encryptProces(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException,
			InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
		return cipher.doFinal(data.getBytes());
	}

	/**
	 * 	 * Method to decypt the data using private key
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(data));
	}

	
	/**
	 * Method to decypt the data using string format private key
	 * 
	 * @param data
	 * @param base64PrivateKey
	 * @return
	 */
	public static String decrypt(String data, String base64PrivateKey) {
		String decryptedString = null;
		try {
			decryptedString = decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
		} catch (IllegalBlockSizeException | InvalidKeyException | BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException exp) {
		} catch (Exception error) {
		}
		return decryptedString;
	}

	
	
	/**
	 * Method to encrypt the data using string format private key
	 * 
	 * @param data
	 * @param publickey
	 * @return
	 */
	 public static String encrypt(String data, String publicKey) {
		String decryptedString = null;

		try {
	        
			publicKey = publicKey.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
			publicKey = publicKey.replaceAll(" ", ""); // Remove any white-spaces.
			publicKey = publicKey.replaceAll("(\\n)", ""); // Remove any "\r\n"			
			decryptedString =  Base64.getEncoder().encodeToString(encryptProces(data, publicKey));
	
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException
				| NoSuchAlgorithmException e) {

		}

		return decryptedString;
	}
	
	
	
	
	/** 
	 *  Method to decrypt the encrypted data using by PHR Public Key.
	 *  
	 * @param data
	 * @return string
	 */
	public static String decrypt(String data) {
		return StringUtils.isEmpty(data) ? null : decrypt(data, JwtTokenUtil.privateKeyContent);
	}

	public static void main(String[] args)
			throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
		try {

			// String plainTextPass =
			// DecryptRSAUtil.decrypt("O+F0BTKrbdThE7Xav2Cb0CPTDoiVKe62ZomvV7XPGBh1h8iVtlwb4xeI0sWrONuW6NHfn8Kpi7EcZhQjTGQEjR1zD2aeaD2nBHnDWIC92BHQe0n96P/a55xFoegSE5BTgvZusLSzJQl69ygJP9DYhfIUx86WJOJnt5yLdrXRZoI=",
			// PRIVATE_KEY)
			String encryptedString = Base64.getEncoder().encodeToString(encryptProces("Abdm@india1122",
					"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7Zq7YKcjmccSBnR9CDHd6IX96V7D/a2XSMs+yCgejSe956mqjA/0Q9h+Xnx7ZZdwe2Tf2Jq/mWXa+gYdnta58otreXg/5oGnNV3Edlixz1Oc8tJg5bG4sIUCGZcbEQGSbm1iC+Fp1kS+YLVG4Su8KoRxcCvRJI2QkfqAruX3JoFjggOkv0TgWCo9z6NV6PPmPN3UsXyH3OPDi3Ewnvd64ngCUKPSBiIDwhLj2yYSShcxH8aWbrz00SJodBJzqgjvCfZuljBXXIN4Ngi/nzqEJ7woKQ1kNgWoHFZy7YL74PihW//4OlniSRoITX+7ChILIv2ezSmAdIjpNJ9Dg9XKcQIDAQAB"));
			System.out.println(encryptedString);
			// String decryptedString =
			// DecryptRSAUtil.decrypt("mwGi4QCAEAOOsCwWCZkFjXM/Wg+nDiIatQhDjAOXnMGvPYlTqeuFaXZMCpsHwUsJH211pWy8n2HqXtWuNUad/ZVU7rLz3DrhgOgm6u58DUzmN+ge82GFZ25MqXw8IFc+2uhqHMfYOFkMWS/zLG9xgfthgXIBbSItRwzo4Q9qxc8=",
			// PRIVATE_KEY);

			// System.out.println(decryptedString);
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
		}

	}
}
