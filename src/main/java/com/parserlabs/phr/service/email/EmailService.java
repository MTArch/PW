/**
 * 
 */
package com.parserlabs.phr.service.email;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.model.email.EmailNotificationRequest;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.response.TransactionResponse;

/**
 * @author Rajesh
 *
 */
@CustomSpanned
public interface EmailService {
	
	public TransactionResponse sendRegistrationOtp(GenerateOTPRequest request, String name);
	public Boolean resendRegistrationOtp(ResendOTPRequest request);
	
	public TransactionResponse sendLoginOtp(EmailNotificationRequest request);

	public TransactionResponse sendAccountUpdateOtp(GenerateOTPRequest request, String name);
	
	public Boolean sendSuccessNotification(EmailNotificationRequest request);


	
}
