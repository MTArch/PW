/**
 * 
 */
package com.parserlabs.phr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrAuthTransactionEntity;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.utils.Argon2Encoder;

/**
 * @author Rajesh
 *
 */
@Component
@CustomSpanned
public class OtpPasswordService {

	/***
	 * Verify The OTP against the transaction in table.
	 * 
	 * @param txn
	 * @param otp
	 * @return true/false
	 */
	public boolean isOtpSame(PhrTransactionEntity txn, String otp) {
		return StringUtils.isNotBlank(txn.getOtp()) ? Argon2Encoder.verify(otp, txn.getOtp()) : false;
	}

	/***
	 * Verify The OTP against the auth transaction in table.
	 * 
	 * @param txn
	 * @param otp
	 * @return true/false
	 */
	public boolean isOtpSame(PhrAuthTransactionEntity authTxn, String otp) {
		return StringUtils.isNotBlank(authTxn.getOtp()) ? Argon2Encoder.verify(otp, authTxn.getOtp()) : false;
	}

	/***
	 * Verify The PWD against the auth transaction in table.
	 * 
	 * @param txn
	 * @param pwd
	 * @return true/false
	 */
	public boolean isPasswordSame(PhrAuthTransactionEntity authTxn, String password) {
		return StringUtils.isNotBlank(authTxn.getPassword()) ? Argon2Encoder.verify(password, authTxn.getPassword())
				: false;
	}

	/**
	 * Verify The PWD against the in table.
	 * 
	 * @param passwordFromDB
	 * @param passwordFromUser
	 * 
	 * @return true/false
	 * 
	 */
	public boolean isPasswordSame(String passwordFromDB, String passwordFromUser) {
		return StringUtils.isNotBlank(passwordFromDB) ? Argon2Encoder.verify(passwordFromUser, passwordFromDB) : false;
	}

}
