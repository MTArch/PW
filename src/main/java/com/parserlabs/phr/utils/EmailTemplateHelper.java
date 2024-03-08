/**
 * 
 */
package com.parserlabs.phr.utils;

import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_GRIEVANCE_URL;
import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_HEADER_NAME;
import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_HOME_PAGE_FB;
import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_HOME_PAGE_LINKEDIN;
import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_HOME_PAGE_TWITTER;
import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_HOME_URL;
import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_NAME;
import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_SUPPORT_ABDM_EMAIL;
import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_SUPPORT_ABDM_EMAIL_MAILTO;
import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_SUPPORT_EMAIL;
import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_ABDM_NAME;
import static com.parserlabs.phr.constants.EmailAuthorityConstants.NHA.AUTHORITY_ABDM_HOME_URL;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.parserlabs.phr.enums.OperationTypeEmail;
import com.parserlabs.phr.model.email.EmailNotificationRequest;
import com.parserlabs.phr.model.email.EmailTemplateContentModel;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@Service
@Slf4j
public class EmailTemplateHelper {

	@Autowired
	private MessageSource messageSource;

	/**
	 * Create the Email template content
	 * 
	 * @param emailTemplateContentModel
	 * @return
	 */

	public Map<String, Object> getTempalteOTP(EmailTemplateContentModel emailTemplateContent) {

		// Create template model
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user_greeting_msg", emailTemplateContent.getUser_greeting_msg());

		model.put("verification_otp", emailTemplateContent.getAction_content());
		model.put("verification_otp_expiretime", PhrUtilits.getExpireTimeString(emailTemplateContent.getExpire_time()));
		model.put("action_url_grievance", AUTHORITY_GRIEVANCE_URL);
		model.put("grievance_link", AUTHORITY_GRIEVANCE_URL);

		// NHA SUPPORT MAIL
		model.put("action_support_email_id", AUTHORITY_SUPPORT_ABDM_EMAIL_MAILTO);
		model.put("support_email_id", AUTHORITY_SUPPORT_ABDM_EMAIL);

		// HOME URL
		model.put("action_url_home_url", emailTemplateContent.getApp_url());

		if (emailTemplateContent.getCodeType().equals(OperationTypeEmail.REGISTRATION)) {
			model.put("heading_message", "Congratulations on signing up for ABHA");
			model.put("heading_message_type", "To complete your profile, please use the following verification code:");
		} else if (emailTemplateContent.getCodeType().equals(OperationTypeEmail.LOGIN)) {
			model.put("heading_message", "Congratulations on login for ABHA");
			model.put("heading_message_type", "To login your profile, please use the following verification code:");
		} else if (emailTemplateContent.getCodeType().equals(OperationTypeEmail.UPDATE)) {
			model.put("heading_message", "Congratulations on update up for ABHA");
			model.put("heading_message_type", "To update your profile, please use the following verification code:");
		}

		return model;

	}

	public Map<String, Object> getNotificationTemplate(EmailTemplateContentModel emailTemplateContent) {

		// Create template model
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user_greeting_msg", emailTemplateContent.getUser_greeting_msg());
		model.put("notification_action_content", emailTemplateContent.getAction_content());
		// NAH GRIEVANCE URL
		model.put("action_url_grievance", AUTHORITY_GRIEVANCE_URL);
		model.put("grievance_link", AUTHORITY_GRIEVANCE_URL);
		model.put("action_nha_home_url", AUTHORITY_HOME_URL);
		// NHA SUPPORT MAIL
		model.put("action_support_email_id", AUTHORITY_SUPPORT_ABDM_EMAIL_MAILTO);
		model.put("support_email_id", AUTHORITY_SUPPORT_ABDM_EMAIL);
		// ABDM
		model.put("abdm_authority", AUTHORITY_ABDM_NAME);
		model.put("abdm_nha_home_url", AUTHORITY_ABDM_HOME_URL);
		// HOME URL
		model.put("action_url_home_url", emailTemplateContent.getApp_url());
		return model;

	}

	public Map<String, Object> getTempalte(EmailTemplateContentModel emailTemplateContentModel) {
		String authority_name = !StringUtils.isEmpty(emailTemplateContentModel.getSender_name())
				? emailTemplateContentModel.getSender_name()
				: AUTHORITY_NAME;// authority name

		// Create template model
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email_header_name", AUTHORITY_HEADER_NAME);

		model.put("appUrl", emailTemplateContentModel.getApp_url());
		// NHA
		model.put("authority_name", authority_name);
		// NHA SUPPORT MAIL
		model.put("action_url_support", AUTHORITY_SUPPORT_EMAIL);

		// NHA SOCIAL MEDIA URL
		model.put("nha_home_facebook", AUTHORITY_HOME_PAGE_FB);
		model.put("nha_home_twitter", AUTHORITY_HOME_PAGE_TWITTER);
		model.put("nha_home_linkedin", AUTHORITY_HOME_PAGE_LINKEDIN);
		// HOME URL
		model.put("action_url_home_url", AUTHORITY_HOME_URL);

		// Control the check and failed gif .css
		model.put("display_success", "display:none;");
		model.put("display_error", "display:none;");

		if (emailTemplateContentModel.getCodeType().equals(OperationTypeEmail.REGISTRATION)
				|| emailTemplateContentModel.getCodeType().equals(OperationTypeEmail.LOGIN)) {

			String[] caption_op_message = new String[1];
			if (emailTemplateContentModel.getCodeType().equals(OperationTypeEmail.REGISTRATION)) {
				caption_op_message[0] = "To Register via your email address";
			} else {
				caption_op_message[0] = "To Login via your registration";
			}

			// Activation Link
			model.put("action_url_activation_link", emailTemplateContentModel.getAction_content());
			// Header Message
			model.put("status_message",
					messageSource.getMessage("email.content.status.message.initiate", null, Locale.ENGLISH));
			// Caption Message
			model.put("status_caption_message",
					messageSource.getMessage("email.content.caption.message", caption_op_message, Locale.ENGLISH));

			// Link Expire warning
			String[] expire_time = new String[1];
			expire_time[0] = PhrUtilits.getExpireTimeString(emailTemplateContentModel.getExpire_time());
			model.put("link_expire_message",
					messageSource.getMessage("email.content.link.expire.message", expire_time, Locale.ENGLISH));
			// Warning
			model.put("link_warn_message",
					messageSource.getMessage("email.content.link.warn.message", null, Locale.ENGLISH));
			// dont reply warning
			model.put("donot_reply_message",
					messageSource.getMessage("email.content.donot.reply.message", null, Locale.ENGLISH));

		} else if (emailTemplateContentModel.getCodeType().equals(OperationTypeEmail.SUCCESS)) {
			model.put("status_message",
					messageSource.getMessage("email.content.status.message.success", null, Locale.ENGLISH));
			model.put("status_sub_message",
					messageSource.getMessage("email.content.verified.success", null, Locale.ENGLISH));
			model.put("display_success", "display:flex;");
		} else if (emailTemplateContentModel.getCodeType().equals(OperationTypeEmail.EXCEPTION)) {
			String[] regen_msg = new String[1];
			regen_msg[0] = "Activation link is Invalid⚠";

			model.put("status_message",
					messageSource.getMessage("email.content.status.message.failed", null, Locale.ENGLISH));
			model.put("status_sub_message",
					messageSource.getMessage("email.content.regenerate.link", regen_msg, Locale.ENGLISH));
			model.put("display_error", "display:flex;");
		} else if (emailTemplateContentModel.getCodeType().equals(OperationTypeEmail.EXPIRE)) {
			String[] regen_msg = new String[1];
			regen_msg[0] = "Activation link is Expired🚫";

			model.put("status_message",
					messageSource.getMessage("email.content.status.message.failed", null, Locale.ENGLISH));
			model.put("status_sub_message",
					messageSource.getMessage("email.content.regenerate.link", regen_msg, Locale.ENGLISH));
			model.put("display_error", "display:flex;");
		} else {
			String[] regen_msg = new String[1];
			regen_msg[0] = "Opps☹, Something went wrong";

			model.put("status_message",
					messageSource.getMessage("email.content.status.message.failed", null, Locale.ENGLISH));
			model.put("status_sub_message",
					messageSource.getMessage("email.content.regenerate.link", regen_msg, Locale.ENGLISH));
			model.put("display_error", "display:flex;");
		}
		return model;
	}

	/**
	 * create the activation link
	 * 
	 * @param userEntity
	 * @param txnResponse
	 * @return
	 * 
	 */
	public String createOTPEmailTemplateMessage(EmailNotificationRequest emailVerifyTxnIdOTP,
			OperationTypeEmail opType) {
		// Create the EMAIL CONTENT TEXT WITH OTP
		StringBuffer action_url_activation_link = new StringBuffer();
		action_url_activation_link.append("OTP is: ");
		action_url_activation_link.append(emailVerifyTxnIdOTP.getOTP());
		log.info("Activation Link: {}", action_url_activation_link.toString());
		return action_url_activation_link.toString();
	}

	/**
	 * create the email text content
	 * 
	 * @param userEntity
	 * @param txnResponse
	 * @return
	 * 
	 */
	public String getEmailActionContent(EmailNotificationRequest request) {
		// Create the EMAIL CONTENT TEXT WITH OTP
		StringBuffer messageContent = new StringBuffer();

		switch (request.getOpType()) {
		case REGISTRATION_OTP:
			String[] messageRegOTP = new String[2];
			messageRegOTP[0] = request.getOTP();
			messageRegOTP[1] = request.getExpireTime();
			messageContent.append(messageSource.getMessage("email.content.registration.message.otp", messageRegOTP, Locale.ENGLISH));
			break;
		case REGISTRATION_SUCCESS:
			String[] messageRegSuccess = new String[2];
			messageRegSuccess[0] = request.getName();
			messageRegSuccess[1] = request.getAbhaAddress();
			messageContent.append(messageSource.getMessage("email.content.registration.message.success", messageRegSuccess, Locale.ENGLISH));
			break;
		case LOGIN_OTP:
			String[] messageLoginOtp = new String[2];
			messageLoginOtp[0] = request.getOTP();
			messageLoginOtp[1] = request.getExpireTime();
			messageContent.append(messageSource.getMessage("email.content.login.message.otp", messageLoginOtp, Locale.ENGLISH));
			break;
		case UPDATE_OTP:
			String[] messageUpdateOTP = new String[2];
			messageUpdateOTP[0] = request.getOTP();
			messageUpdateOTP[1] = request.getExpireTime();
			messageContent.append(
					messageSource.getMessage("email.content.update.message.otp", messageUpdateOTP, Locale.ENGLISH));
			break;
		case UPDATE_SUCCESS:
			String[] messageUpdateSuccess = new String[2];
			messageUpdateSuccess[0] = request.getName();
			messageUpdateSuccess[1] = request.getAbhaAddress();
			messageContent.append(messageSource.getMessage("email.content.update.message.success", messageUpdateSuccess, Locale.ENGLISH));
			break;
		case LINK_HID_OTP:
			String[] messageLinkHidOtp = new String[2];
			messageLinkHidOtp[0] = request.getOTP();
			messageLinkHidOtp[1] = request.getExpireTime();
			messageContent.append(messageSource.getMessage("email.content.link.abha.message.otp", messageLinkHidOtp, Locale.ENGLISH));
			break;
		case LINK_HID_SUCCESS:
			String[] messageLinkHidSuccess = new String[3];
			messageLinkHidSuccess[0] = request.getName();
			messageLinkHidSuccess[1] = request.getAbhaNumber();
			messageLinkHidSuccess[2] = request.getAbhaAddress();
			messageContent.append(messageSource.getMessage("email.content.link.abha.message.success", messageLinkHidSuccess, Locale.ENGLISH));
			break;
		case DELINK_HID_OTP:
			String[] messageDeLinkHidOtp = new String[2];
			messageDeLinkHidOtp[0] = request.getOTP();
			messageDeLinkHidOtp[1] = request.getExpireTime();
			messageContent.append(messageSource.getMessage("email.content.delink.abha.message.otp", messageDeLinkHidOtp, Locale.ENGLISH));
			break;
		case DELINK_HID_SUCCESS:
			String[] messageDeLinkHidSuccess = new String[2];
			messageDeLinkHidSuccess[0] = request.getAbhaNumber();
			messageDeLinkHidSuccess[1] = request.getAbhaAddress();
			messageContent.append(messageSource.getMessage("email.content.delink.abha.message.success", messageDeLinkHidSuccess, Locale.ENGLISH));
			break;
		default:
			break;
		}

		return messageContent.toString();
	}

	/**
	 * get the Subject of email
	 * 
	 * @param userEntity
	 * @param txnResponse
	 * @return
	 * 
	 */
	public String getEmailSubject(EmailNotificationRequest request) {
		// Create the EMAIL CONTENT TEXT WITH OTP
		StringBuffer messageSubject = new StringBuffer();

		switch (request.getOpType()) {
		case REGISTRATION_OTP:
			messageSubject
					.append(messageSource.getMessage("email.content.registration.subject.otp", null, Locale.ENGLISH));
			break;
		case REGISTRATION_SUCCESS:
			messageSubject.append(
					messageSource.getMessage("email.content.registration.subject.success", null, Locale.ENGLISH));
			break;
		case UPDATE_OTP:
			messageSubject
					.append(messageSource.getMessage("email.content.update.subject.otp", null, Locale.ENGLISH));
			break;
		case UPDATE_SUCCESS:
			messageSubject.append(
					messageSource.getMessage("email.content.update.subject.success", null, Locale.ENGLISH));
			break;
		case LOGIN_OTP:
			messageSubject.append(messageSource.getMessage("email.content.login.subject.otp", null, Locale.ENGLISH));
			break;
		case LINK_HID_OTP:
			messageSubject
					.append(messageSource.getMessage("email.content.link.subject.otp", null, Locale.ENGLISH));
			break;
		case LINK_HID_SUCCESS:
			messageSubject
					.append(messageSource.getMessage("email.content.link.subject.success", null, Locale.ENGLISH));
			break;
		case DELINK_HID_OTP:
			messageSubject
					.append(messageSource.getMessage("email.content.delink.subject.otp", null, Locale.ENGLISH));
			break;
		case DELINK_HID_SUCCESS:
			messageSubject.append(
					messageSource.getMessage("email.content.delink.subject.success", null, Locale.ENGLISH));
			break;
		default:
			break;
		}
		return messageSubject.toString();
	}

	/**
	 * create the activation link
	 * 
	 * @param userEntity
	 * @param txnResponse
	 * @return
	 * 
	 */
	public String createOTPEmailTemplateMessage(EmailNotificationRequest emailVerifyTxnIdOTP) {
		// Create the EMAIL CONTENT TEXT WITH OTP
		StringBuffer action_url_activation_link = new StringBuffer();
		action_url_activation_link.append(emailVerifyTxnIdOTP.getOTP());
		return action_url_activation_link.toString();
	}

}
