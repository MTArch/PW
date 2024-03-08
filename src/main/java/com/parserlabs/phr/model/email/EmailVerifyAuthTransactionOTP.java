/**
 * 
 */
package com.parserlabs.phr.model.email;

import com.parserlabs.phr.entity.PhrAuthTransactionEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rajesh
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerifyAuthTransactionOTP {
	
	private PhrAuthTransactionEntity phrAuthTransactionEntity;
	private String OTP;

}
