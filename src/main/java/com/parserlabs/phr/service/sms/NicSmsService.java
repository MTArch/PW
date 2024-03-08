package com.parserlabs.phr.service.sms;

import static com.parserlabs.phr.config.security.ApiAccessRequestFilter.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.enums.SMSTypeEnums;
import com.parserlabs.phr.service.KafkaService;

import lombok.extern.slf4j.Slf4j;

/*
 * Rajesh Kumar
 * SMS Service to send SMS via NIC GW.
 */

@Service("SMSService")
@Slf4j
@CustomSpanned
public class NicSmsService implements SMSService {

	@Autowired
	HttpServletRequest httpRequest;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private KafkaService kafkaSmsService;

	@Value("${smsservice.enabled:false}")
	private boolean otpEnabled;

	@Value("${app.home}")
	private String applicationUrl;

	@Value("${app.entity-id}")
	private String entityId;

	@Value("${app.phr.sms.account: abhaaddresssms}")
	private String phrIdSmsAccount;

	@Value("${app.phr.otp.account: abhaaddressotp}")
	private String phrIdOtpAccount;

	@Value("${app.phr.sms.pwd}")
	private String phrIddSmsPwd;

	@Value("${app.phr.otp.pwd}")
	private String phrIdOtpPwd;
	
	@Value("${app.phr.mobile.sms.account}")
	private String phrMobileSmsAccount;
	
	@Value("${app.phr.mobile.otp.account}")
	private String phrMobileIdOtpAccount;
	
	@Value("${app.phr.mobile.sms.pwd}")
	private String phrMobileSmsPwd;
	
	@Value("${app.phr.mobile.otp.pwd}") 
	private String phrMobileIdOtpPwd;

	@Value("${opt.verification.expire.minutes:10}")
	private String otpExpireInminutes;

	@Value("${opt.verification.write.tous:abdm@nha.gov.in}")
	private String complaintEmail;

	private static final String SIGNATURE_HEALTH_ID = "NHASMS";

	@Override
	@Async
	public boolean sendSMS(String phoneNumber, String message) {

		boolean result = true;
       
		if (otpEnabled) {

			if(IS_MOBILE){
         
				send(phoneNumber, message(message), phrMobileSmsAccount, phrMobileSmsPwd, templeId(message));

			}else{

				send(phoneNumber, message(message), phrIdSmsAccount, phrIddSmsPwd, templeId(message));

			}

		} else {

			log.info("SMS Service disabled.");

		}

		return result;

		}

	@Override
	@Async
	public boolean sendOTP(String phoneNumber, String message) {
		boolean result = true;
		if (otpEnabled) {
			
			if(IS_MOBILE){

				send(phoneNumber, message(message), phrMobileIdOtpAccount, phrMobileIdOtpPwd, templeId(message));
		
			}else{

				send(phoneNumber, message(message), phrIdOtpAccount, phrIdOtpPwd, templeId(message));

			}

		}
       
       
		return result;

		}
	@Override
	public boolean send(String phoneNumber, String message, String login, String pw, String templeId) {

		try {

			kafkaSmsService.send(phoneNumber, message, login, pw, templeId, entityId, SIGNATURE_HEALTH_ID);

			log.info("SMS Sent: To mobile Number: {} TemplateId:{}", phoneNumber, templeId);
		} catch (Exception ex) {
			log.error("SMS Error: To mobile Number: {} TemplateId:{} GWResponse:{}", phoneNumber, templeId,
					ex.toString());
			return false;
		}
		return true;
	}

//	@Override
//	public boolean sendHealthIdSuccessNotification(HidResponse user) {
//		// Do not send notifications to existing users.
//		if (!user.isNew()) {
//			return false;
//		}
//		String[] messageParams = new String[4];
//		messageParams[0] = user.getName();
//		// messageParams[1] = user.getHealthId();
//		messageParams[1] = user.getHealthIdNumber();
//		messageParams[2] = applicationUrl;
//		log.info("Sending success notification msg to : {}", user.getMobile());
//		return sendSMS(user.getMobile(),
//				messageSource.getMessage("registration.success.otp", messageParams, httpRequest.getLocale()));
//	}

	@Override
	public boolean sendAuthOtp(String phoneNumber, String otp) {
		String[] messageParams = new String[1];
		messageParams[0] = otp;
		log.info("Sending auth msg to : {}", phoneNumber);
		return sendOTP(phoneNumber,
				messageSource.getMessage("authentication.otp.msg", messageParams, httpRequest.getLocale()));
	}

	@Override
	public boolean sendVerificationOtp(String phoneNumber, String otp) {
		String[] messageParams = new String[2];
		messageParams[0] = otp;
		messageParams[1] = otpExpireInminutes;
		log.info("Sending verification msg to {}", phoneNumber);
		return sendOTP(phoneNumber,
				messageSource.getMessage("registration.otp.msg", messageParams, httpRequest.getLocale()));
	}

	@Override
	public boolean sendUpdateVerificationOtp(String phoneNumber, String otp) {
		String[] messageParams = new String[2];
		messageParams[0] = otp;
		messageParams[1] = otpExpireInminutes;
		log.info("Sending verification msg to {}", phoneNumber);
		return sendOTP(phoneNumber,
				messageSource.getMessage("hid.phr.update.msg", messageParams, httpRequest.getLocale()));
	}
	@Override
	public boolean forgotHidOtp(String phoneNumber, String otp) {
		String[] messageParams = new String[1];
		messageParams[0] = otp;
		log.info("Sending hid recovery msg to : {}", phoneNumber);
		return sendOTP(phoneNumber,
				messageSource.getMessage("forgot.hid.otp.msg", messageParams, httpRequest.getLocale()));
	}

	@Override
	public boolean sendBenefitIntegrationsNotification(String healthIdNumber, String phoneNumber, String benefitName) {
		String[] messageParams = new String[2];
		messageParams[0] = benefitName;
		messageParams[1] = applicationUrl;
		log.info("Sending hid benefit : {} integration msg to : {} on {}", healthIdNumber, benefitName, phoneNumber);
		return sendSMS(phoneNumber,
				messageSource.getMessage("hid.notifybenefit.msg", messageParams, httpRequest.getLocale()));
	}

	@Override
	public boolean sendHealthIdSuccessNotification(String userName, String phrAdress, String phoneNumber) {
		String[] messageParams = new String[3];
		messageParams[0] = userName;
		messageParams[1] = phrAdress;
		messageParams[2] = applicationUrl;
		log.info("Sending success notification msg to : {}", phoneNumber);
		return sendSMS(phoneNumber,
				messageSource.getMessage("registration.success.otp", messageParams, httpRequest.getLocale()));
	}

	@Override
	public boolean sendHealthIdDeactivationNotification(String name, String healthIdNumber, String phoneNumber) {
		String[] messageParams = new String[3];
		messageParams[0] = name;
		messageParams[1] = healthIdNumber;
		messageParams[2] = applicationUrl;
		log.info("Sending account deactivation notification msg to : {}", phoneNumber);
		return sendSMS(phoneNumber,
				messageSource.getMessage("hid.account.deactivate.msg", messageParams, httpRequest.getLocale()));
	}

	@Override
	public boolean sendHealthIdReactivicationNotification(String name, String healthIdNumber, String phoneNumber) {
		String[] messageParams = new String[3];
		messageParams[0] = name;
		messageParams[1] = healthIdNumber;
		log.info("Sending account reactivation notification msg to : {}", phoneNumber);
		return sendSMS(phoneNumber,
				messageSource.getMessage("hid.account.reactivate.msg", messageParams, httpRequest.getLocale()));
	}

	@Override
	public boolean sendHealthIdDeletionNotification(String name, String healthIdNumber, String phoneNumber) {
		String[] messageParams = new String[3];
		messageParams[0] = name;
		messageParams[1] = healthIdNumber;
		log.info("Sending account Deletion notification msg to : {}", phoneNumber);
		return sendSMS(phoneNumber,
				messageSource.getMessage("hid.account.delete.msg", messageParams, httpRequest.getLocale()));
	}

	@Override
	public boolean sendPhrLinkedNotification(String abhaNumber, String abhaAddress, String phoneNumber) {
		String[] messageParams = new String[2];
		messageParams[0] = abhaNumber;
		messageParams[1] = abhaAddress;
		log.info("Sending account PHR Linked notification msg to : {}", phoneNumber);
		return sendSMS(phoneNumber,
				messageSource.getMessage("hid.phr.link.msg", messageParams, httpRequest.getLocale()));
	}

	@Override
	public boolean sendPhrUnLinkedNotification(String abhaNumber, String abhaAddress, String phoneNumber) {
		String[] messageParams = new String[2];
		messageParams[0] = abhaNumber;
		messageParams[1] = abhaAddress;
		log.info("Sending account PHR UNLINKED notification msg to : {}", phoneNumber);
		return sendSMS(phoneNumber,
				messageSource.getMessage("hid.phr.unlink.msg", messageParams, httpRequest.getLocale()));
	}

	private String templeId(String message) {
		return !StringUtils.isEmpty(message) ? StringUtils.substringAfter(message, "##") : null;
	}

	private String message(String message) {
		return !StringUtils.isEmpty(message) ? StringUtils.substringBefore(message, "##") : null;
	}

	@Override
	public boolean sendOtpAuthenticationSMS(String phoneNumber, String otp, SMSTypeEnums smsType) {
		String[] messageParams = new String[2];
		messageParams[0] = otp;
		messageParams[1] = otpExpireInminutes;

		String messageStr;
		switch (smsType) {
		case LOGIN_OTP:
			messageStr = messageSource.getMessage("authentication.otp.msg", messageParams, httpRequest.getLocale());
			log.info("Sending login OTP msg to : {}", phoneNumber);
			break;
		case UPDATE_OTP:
			messageStr = messageSource.getMessage("hid.phr.update.msg", messageParams, httpRequest.getLocale());
			log.info("Sending Update OTP msg to : {}", phoneNumber);
			break;
		default:
			return false;
		}

		return sendOTP(phoneNumber, messageStr);
	}

	@Override
	public boolean sendSuccessNotification(String fName, String LName, String abhaAddress, String abhaNumber,
			String phoneNumber, SMSTypeEnums smsType) {

		switch (smsType) {
		case REGISTER_SUCCESS_EMAIL_MOBILE:
			String[] messageParams = new String[4];
			messageParams[0] = fName;
			messageParams[1] = LName;
			messageParams[2] = abhaAddress;
			messageParams[3] = complaintEmail;
			log.info("Sending account creation via e,m notification msg to : {}", phoneNumber);
			return sendSMS(phoneNumber, messageSource.getMessage("hid.phr.register.success.mobileemail.msg",
					messageParams, httpRequest.getLocale()));

		case REGISTER_SUCCESS_ABHA_NUMBER:
			String[] messageParams1 = new String[5];
			messageParams1[0] = fName;
			messageParams1[1] = LName;
			messageParams1[2] = abhaAddress;
			messageParams1[3] = abhaNumber;
			messageParams1[4] = complaintEmail;
			log.info("Sending account creation via e,m notification msg to : {}", phoneNumber);
			return sendSMS(phoneNumber, messageSource.getMessage("hid.phr.register.success.abhanumber.msg",
					messageParams1, httpRequest.getLocale()));

		default:
			return false;
		}

	}

}
