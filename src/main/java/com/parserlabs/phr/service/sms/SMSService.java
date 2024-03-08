package com.parserlabs.phr.service.sms;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.enums.SMSTypeEnums;

/**
 * @author Rajesh SMS interface
 */
@CustomSpanned
public interface SMSService {

	boolean send(String phoneNumber, String message, String login, String pw, String templeId);

	boolean sendSMS(String phoneNumber, String message);

	boolean sendOTP(String phoneNumber, String message);

	boolean sendAuthOtp(String phoneNumber, String otp);

	boolean sendVerificationOtp(String phoneNumber, String otp);
	
    boolean sendUpdateVerificationOtp(String phoneNumber,String otp);
//    boolean sendHealthIdSuccessNotification(HidResponse user);

	boolean sendBenefitIntegrationsNotification(String healthIdNumber, String phoneNumber, String benefitName);

	boolean forgotHidOtp(String phoneNumber, String otp);

	boolean sendHealthIdSuccessNotification(String userName, String healthIdNumber, String phoneNumber);

	boolean sendHealthIdDeactivationNotification(String name, String healthIdNumber, String phoneNumber);

	boolean sendHealthIdReactivicationNotification(String name, String healthIdNumber, String phoneNumber);

	boolean sendHealthIdDeletionNotification(String name, String healthIdNumber, String phoneNumber);

	boolean sendPhrLinkedNotification(String abhaNumber, String abhaAddress, String phoneNumber);

	boolean sendPhrUnLinkedNotification(String abhaNumber, String abhaAddress, String phoneNumber);

	boolean sendOtpAuthenticationSMS(String phoneNumber, String otp, SMSTypeEnums smsType);
	
	boolean sendSuccessNotification(String fName, String LName, String abhaAddress,  String abhaNumber, String phoneNumber, SMSTypeEnums smsType);

}
