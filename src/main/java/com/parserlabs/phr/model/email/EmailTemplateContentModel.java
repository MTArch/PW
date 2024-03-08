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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTemplateContentModel {

	// LINK
	private OperationTypeEmail codeType;
	private String message;
	private String action_content;
	private String status_message;
	private String status_sub_message;
	private String app_url;
	private String sender_name;
	private String to_email_address;
	private long expire_time;

	// OTP
	private String user_greeting_msg;
	private String verification_otp;
	private String verification_otp_expiretime;
	private String action_url_grievance;
	private String grievance_link;
	
	//
	private String abhaNumber;
	private String abhaAddress;

}
