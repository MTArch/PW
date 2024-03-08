/**
 * 
 */
package com.parserlabs.phr.service.email;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.enums.EmailTypeEnum;
import com.parserlabs.phr.enums.OperationTypeEmail;
import com.parserlabs.phr.exception.EmailVerificationException;
import com.parserlabs.phr.model.email.EmailContentModel;
import com.parserlabs.phr.model.email.EmailNotificationRequest;
import com.parserlabs.phr.model.email.EmailTemplateContentModel;
import com.parserlabs.phr.model.notification.Mail;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.service.KafkaService;
import com.parserlabs.phr.service.TransactionService;
import com.parserlabs.phr.utils.EmailTemplateHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@Service
@Slf4j
@CustomSpanned
@ConditionalOnExpression("${email.verification.enabled:true}")
public class EmailNotificationService implements EmailService {

	private final String ACTIVATION_NOTIFICATION_TEMPLATE = "email-otp-notification-template";

	@Value("${email.verification.expire.time:120}")
	private long NOTIFICATION_EXPIRE_LINK;

	@Value("${email.verification.otp.expire.time:10}")
	private long NOTIFICATION_OTP_EXPIRE_INMINUTES;

	@Value("${email.verification.sendermail}")
	private String senderAddress;

	@Value("${email.verification.sendername}")
	private String senderName;

	@Value("${app.home}")
	private String appUrl;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private KafkaService kafkaServie;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private EmailTemplateHelper emailTemplateHelper;

	@Override
	public TransactionResponse sendRegistrationOtp(GenerateOTPRequest request, String name) {

		// 1: Validate email not associated with other account
		// checkEmailNotExistWithOtherAccount(request.getValue());

		EmailNotificationRequest emailVerifyTxnIdOTP = transactionService.getEmailVerificationTxnAndOTP(request);
		emailVerifyTxnIdOTP.setOpType(OperationTypeEmail.REGISTRATION_OTP);
		emailVerifyTxnIdOTP.setName(name);
		// Send the Verification Mail with OTP
		return sendEmailOTP(emailVerifyTxnIdOTP, EmailTypeEnum.VERIFICATION_OTP);
	}

	@Override
	public Boolean resendRegistrationOtp(ResendOTPRequest request) {
		EmailNotificationRequest emailVerifyTxnIdOTP = transactionService
				.resendVerificationEmailOtp(request.getTransactionId());
		emailVerifyTxnIdOTP.setOpType(OperationTypeEmail.REGISTRATION_OTP);

		// Send the Verification Mail with OTP
		TransactionResponse response = sendEmailOTP(emailVerifyTxnIdOTP, EmailTypeEnum.VERIFICATION_OTP);
		return Objects.nonNull(response.getTransactionId()) ? true : false;
	}

	/**
	 * Send the Login OTP Via EMAIL
	 * 
	 * @implNote Default template is Notification
	 * @param EmailNotificationRequest
	 */
	@Override
	public TransactionResponse sendLoginOtp(EmailNotificationRequest request) {
		return sendEmailOTP(request, EmailTypeEnum.VERIFICATION_OTP);
	}

	/***
	 * Send OTP for Account update verification on EMail Address
	 * 
	 * @param generateOTPRequest
	 * @param String             (user name)
	 * 
	 * @return TransactionResponse
	 */
	@Override
	public TransactionResponse sendAccountUpdateOtp(GenerateOTPRequest request, String name) {

		EmailNotificationRequest emailVerifyTxnIdOTP = transactionService.getEmailVerificationTxnAndOTP(request);
		emailVerifyTxnIdOTP.setOpType(OperationTypeEmail.UPDATE_OTP);
		emailVerifyTxnIdOTP.setName(name);
		// Send the Verification Mail with OTP
		return sendEmailOTP(emailVerifyTxnIdOTP, EmailTypeEnum.VERIFICATION_OTP);
	}

	/**
	 * Method to validate the User and Send the ActivationLink Email
	 * 
	 * @param payload
	 */
	private TransactionResponse sendEmailOTP(EmailNotificationRequest request, EmailTypeEnum notificationType) {
		boolean isSuccess = false;
		// Send the email Registration OTP
		isSuccess = sendNotificationEmail(request, notificationType);
		if (!isSuccess) {
			throwException("Something went wrong. Please try after sometime",
					"Unable to send Eamil. txid : " + request.getTransactionId());
		}
		return TransactionResponse.builder().transactionId(request.getTransactionId()).build();
	}

	@Override
	public Boolean sendSuccessNotification(EmailNotificationRequest request) {
		// Send the Verification Mail with OTP
		TransactionResponse response = sendEmailOTP(request, EmailTypeEnum.NOTIFICATION);
		return Objects.nonNull(response.getTransactionId()) ? true : false;
	}

	/**
	 * Method will send the email verification activation link to the user email
	 * address
	 * 
	 * @param EmailNotificationTxnOtp
	 * @param txnEntity
	 * @return
	 * 
	 */
	private boolean sendNotificationEmail(EmailNotificationRequest emailVerifyTxnIdOTP,
			EmailTypeEnum notificationType) {
		return sendNotificationEmail(getEmailContentModel(emailVerifyTxnIdOTP, notificationType));
	}

	private boolean sendNotificationEmail(EmailContentModel emailContentModel) {
		log.info("START... Sending email: {}", emailContentModel.getToAddress());
		try {
			Mail mail = new Mail();
			mail.setFrom(emailContentModel.getFromAddress()); // whom to send email
			mail.setMailTo(emailContentModel.getToAddress()); // who send the email
			mail.setSubject(emailContentModel.getSubject()); // subject
			mail.setSenderName(emailContentModel.getSenderName());
			mail.setProps(emailContentModel.getTemplateProps());
			mail.setMailContent(emailContentModel.getTemplateContent());

			kafkaServie.sendEmail(mail); // Send the Mail
			log.info("END... Email forwarded to notification servcies: {}", emailContentModel.getToAddress());
			return true;
		} catch (Exception e) {
			log.error("Exception Verification E-mail: {}", e);
		}
		return false;
	}

	private EmailContentModel getEmailContentModel(EmailNotificationRequest request, EmailTypeEnum emailVerificationType) {
		String toAddress = request.getEmail();
		String fromAddress = senderAddress;
		String sender_Name = senderName;
		String subject = emailTemplateHelper.getEmailSubject(request);
		
		OperationTypeEmail opType = request.getOpType();

		// Creation the OTP Content
		String action_content = null;
		String TEMPLATE_TYPE = null;
		long NOTIFICATION_CONTENT_EXPIRE = 0;

		// Both Case Template are same for now. Changes Here to update the Template and
		// Its Props.
		switch (emailVerificationType) {
		case VERIFICATION_OTP:
			NOTIFICATION_CONTENT_EXPIRE = NOTIFICATION_OTP_EXPIRE_INMINUTES;
			request.setExpireTime(String.valueOf(NOTIFICATION_CONTENT_EXPIRE));
			action_content = emailTemplateHelper.getEmailActionContent(request);
			TEMPLATE_TYPE = ACTIVATION_NOTIFICATION_TEMPLATE;
			break;
		case NOTIFICATION:
			NOTIFICATION_CONTENT_EXPIRE = NOTIFICATION_OTP_EXPIRE_INMINUTES;
			request.setExpireTime(String.valueOf(NOTIFICATION_CONTENT_EXPIRE));
			action_content = emailTemplateHelper.getEmailActionContent(request);
			TEMPLATE_TYPE = ACTIVATION_NOTIFICATION_TEMPLATE;
			break;

		default:
			break;
		}

		// GET THE TEMPLATE PROPERTIES
		Map<String, Object> templateProps = emailTemplateHelper.getNotificationTemplate(
				EmailTemplateContentModel.builder().action_content(action_content).codeType(opType)
						.expire_time(NOTIFICATION_CONTENT_EXPIRE).sender_name(sender_Name).to_email_address(toAddress)
						.app_url(appUrl).user_greeting_msg(populateGreetingMessage(request.getName())).build());

		return EmailContentModel.builder().toAddress(toAddress).fromAddress(fromAddress).senderName(sender_Name)
				.subject(subject).templateProps(templateProps)
				.templateContent(getContentTemplate(TEMPLATE_TYPE, templateProps)).build();

	}

	private String populateGreetingMessage(String name) {
		String WELCOME = "WELCOME ";
		return WELCOME.concat(StringUtils.isNotBlank(name) ? name : "");
	}

	/**
	 * Method to get the template content from the thymeleaf template engine
	 * 
	 * @param templateName Map<String, Object>
	 * @return string
	 */
	private String getContentTemplate(String templateName, Map<String, Object> templateModel) {
		Context context = new Context();
		context.setVariables(templateModel);
		return templateEngine.process(templateName, context);
	}

	/**
	 * exception thrown
	 * 
	 * @param exp
	 */
	private void throwException(String exp_message, String log_message) {
		log.info(!StringUtils.isEmpty(log_message) ? log_message : exp_message);
		throw new EmailVerificationException("$$" + exp_message);

	}

}
