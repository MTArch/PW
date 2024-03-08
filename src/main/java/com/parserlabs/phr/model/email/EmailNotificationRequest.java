/**
 * 
 */
package com.parserlabs.phr.model.email;

import com.parserlabs.phr.enums.OperationTypeEmail;

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
public class EmailNotificationRequest {
	
	private String transactionId;
	private String OTP;
	private String email;
	private OperationTypeEmail opType;
	private String name;
	
	private String expireTime;

	private String abhaAddress;
	private String abhaNumber;

}
