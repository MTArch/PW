/**
 * 
 */
package com.parserlabs.phr.utils;

import com.password4j.Hash;
import com.password4j.Password;

import lombok.experimental.UtilityClass;

/**
 * @author Rajesh
 *
 */
@UtilityClass
public class Argon2Encoder {

	private String GLOBAL_PEPPER = "HEALTH_ID";

	/**
	 * Encode the input(OTP/passwords)using the argon2 algorithm
	 * 
	 * @apiNote https://github.com/Password4j/password4j
	 * @param OTP
	 * @param hp_id
	 * @return String
	 */
	public String encode(String input, String saltValue) {
		String rawInput = input;
		String salt = saltValue;
		Hash argon2Hash = Password.hash(rawInput).addSalt(salt).addPepper(GLOBAL_PEPPER).withArgon2();
		return argon2Hash.getResult();
	}

	/**
	 * Verify the input(OTP/Password) using the Argon2 algorithm
	 * 
	 * @param userSubmittedOTP
	 * @param hashFromDB
	 * @return bool
	 */
	public static boolean verify(String userSubmittedInput, String hashFromDB) {
		return Password.check(userSubmittedInput, hashFromDB).addPepper(GLOBAL_PEPPER).withArgon2();

	}
}
