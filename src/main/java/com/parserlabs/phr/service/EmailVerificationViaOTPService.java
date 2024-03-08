/**
 * 
 */
package com.parserlabs.phr.service;

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
import com.parserlabs.phr.model.email.EmailNotificationRequest;
import com.parserlabs.phr.model.email.EmailTemplateContentModel;
import com.parserlabs.phr.model.notification.Mail;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.repository.UserRepository;
import com.parserlabs.phr.utils.EmailTemplateHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh Kumar
 *
 */
@Deprecated
@Service
@Slf4j
@CustomSpanned
@ConditionalOnExpression("${email.verification.enabled:true}")
public class EmailVerificationViaOTPService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private KafkaService kafkaServie;

	@Autowired
	private EmailTemplateHelper emailTemplateHelper;

	@Value("${email.verification.expire.time:120}")
	private long NOTIFICATION_EXPIRE_LINK;

	@Value("${email.verification.otp.expire.time:10}")
	private long NOTIFICATION_OTP_EXPIRE_INMINUTES;

	@Value("${email.verification.sendermail}")
	private String senderAddress;

	@Value("${email.verification.sendername}")
	private String senderName;

	@Value("${email.verification.subject}")
	private String senderSubject;

	@Value("${app.home}")
	private String appUrl;

	private final String ACTIVATION_VERIFY_TEMPLATE = "emailverification-activation-template";

	private final String ACTIVATION_VERIFY_OTP_TEMPLATE = "emailverification-otp-template";

	// REGION Initiate the VERIFICATION EMAIL OTP

	/**
	 * Generate the transaction number
	 * 
	 * @param request
	 * @return
	 */

	public TransactionResponse startEmailOTPTranasction(GenerateOTPRequest request, String name) {
		// DEFAULT type is Registration
		return startEmailOTPTranasction(request, OperationTypeEmail.REGISTRATION, name);
	}

	public TransactionResponse startEmailOTPTranasction(GenerateOTPRequest request,
			OperationTypeEmail operationTypeEmail, String name) {
		// 1: Validate email not associated with other account
		// checkEmailNotExistWithOtherAccount(request.getValue());

		EmailNotificationRequest emailVerifyTxnIdOTP = transactionService.getEmailVerificationTxnAndOTP(request);
		emailVerifyTxnIdOTP.setOpType(operationTypeEmail);
		emailVerifyTxnIdOTP.setName(name);
		// Send the Verification Mail with OTP
		return sentEmailVerificationOTP(emailVerifyTxnIdOTP);

	}

	/**
	 * Method to validate the User and Send the ActivationLink Email
	 * 
	 * @param payload
	 */
	public TransactionResponse sentEmailVerificationOTP(EmailNotificationRequest emailVerifyTxnIdOTP) {

		boolean isSuccess = false;
		// Send the email Registration OTP
		isSuccess = sendVerificationEmail(emailVerifyTxnIdOTP);
		if (!isSuccess) {
			throwException("Something went wrong. Please try after sometime",
					"Unable to send Eamil. txid : " + emailVerifyTxnIdOTP.getTransactionId());
		}
		return TransactionResponse.builder().transactionId(emailVerifyTxnIdOTP.getTransactionId()).build();
	}

	/**
	 * Method will send the email verification activation link to the user email
	 * address
	 * 
	 * @param userEntity
	 * @param txnEntity
	 * @return
	 * 
	 */

	private boolean sendVerificationEmail(EmailNotificationRequest emailVerifyTxnIdOTP) {
		return sendVerificationEmail(emailVerifyTxnIdOTP, EmailTypeEnum.VERIFICATION_OTP);
	}

	private boolean sendVerificationEmail(EmailNotificationRequest emailVerifyTxnIdOTP,
			EmailTypeEnum emailVerificationType) {
		String toAddress = emailVerifyTxnIdOTP.getEmail();
		String fromAddress = senderAddress;
		String sender_Name = senderName;
		String subject = senderSubject;
		OperationTypeEmail opType = emailVerifyTxnIdOTP.getOpType();

		// Creation the OTP Content
		String action_content;
		String TEMPLATE_TYPE;
		long NOTIFICATION_CONTENT_EXPIRE;

		switch (emailVerificationType) {
		case VERIFICATION_LINK:
			action_content = createOTPEmailTemplateMessage(emailVerifyTxnIdOTP, opType);
			TEMPLATE_TYPE = ACTIVATION_VERIFY_TEMPLATE;
			NOTIFICATION_CONTENT_EXPIRE = NOTIFICATION_EXPIRE_LINK;
			break;
		case VERIFICATION_OTP:
			action_content = createOTPEmailTemplateOTPMessage(emailVerifyTxnIdOTP);
			TEMPLATE_TYPE = ACTIVATION_VERIFY_OTP_TEMPLATE;
			NOTIFICATION_CONTENT_EXPIRE = NOTIFICATION_OTP_EXPIRE_INMINUTES;
			break;
		default:
			log.info("TEMPLATE TYPE is invalid");
			return false;
		}

		EmailTemplateContentModel emailTemplateContentModel = EmailTemplateContentModel.builder()
				.action_content(action_content).codeType(opType).expire_time(NOTIFICATION_CONTENT_EXPIRE)
				.sender_name(sender_Name).to_email_address(toAddress).app_url(appUrl)
				.user_greeting_msg(populateGreetingMessage(emailVerifyTxnIdOTP.getName())).build();

		log.info("START... Sending email on {}", toAddress);
		try {
			Mail mail = new Mail();
			mail.setFrom(fromAddress); // whom to send email
			mail.setMailTo(toAddress); // who send the email
			mail.setSubject(subject); // subject
			mail.setSenderName(sender_Name);
			mail.setProps(emailTemplateHelper.getTempalteOTP(emailTemplateContentModel));
			mail.setMailContent(getContentTemplate(TEMPLATE_TYPE, mail.getProps()));

			kafkaServie.sendEmail(mail); // Send the Mail
			log.info("END... Email successfully forwarded to notification servcies for {}", toAddress);
			return true;
		} catch (Exception e) {
			log.error("Exception while sending the Verification E-mail: {}", e);
		}
		return false;
	}

	/**
	 * create the activation link
	 * 
	 * @param userEntity
	 * @param txnResponse
	 * @return
	 * 
	 */
	private String createOTPEmailTemplateMessage(EmailNotificationRequest emailVerifyTxnIdOTP, OperationTypeEmail opType) {

		// TODO ADD THE NEW TEMPALTE OF EMAIL IN REGISRATION
		// Create the EMAIL CONTENT TEXT WITH OTP
		StringBuffer action_url_activation_link = new StringBuffer();
		action_url_activation_link.append("OTP is: ");
		action_url_activation_link.append(emailVerifyTxnIdOTP.getOTP());
		log.info("Activation Link: {}", action_url_activation_link.toString());
		return action_url_activation_link.toString();
	}

	/**
	 * create the activation link
	 * 
	 * @param userEntity
	 * @param txnResponse
	 * @return
	 * 
	 */
	private String createOTPEmailTemplateOTPMessage(EmailNotificationRequest emailVerifyTxnIdOTP) {
		// Create the EMAIL CONTENT TEXT WITH OTP
		StringBuffer action_url_activation_link = new StringBuffer();
		action_url_activation_link.append(emailVerifyTxnIdOTP.getOTP());
		return action_url_activation_link.toString();
	}

	/**
	 * Check email not exist in the different account
	 * 
	 * @param userEntity
	 * @param request
	 * 
	 */
	@SuppressWarnings("unused")
	private void checkEmailNotExistWithOtherAccount(String email) {
		boolean isexist = userRepository.existsByEmail(email.toLowerCase()).orElse(false);
		if (isexist) {
			throwException(String.format("An email '%s' already used, Please try different e-mail address.",
					email.toLowerCase()), "Email already in use.");
		}

	}

	// ENDREGION

	// REGION OTP VERIFY

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


	/**
	 * RE-Send the Verification OTP to the EMAIL.
	 * 
	 * @param resendOTPRequest
	 * @return true/false
	 */
	public boolean resendOtpEmailVerification(ResendOTPRequest request) {
		EmailNotificationRequest emailVerifyTxnIdOTP = transactionService
				.resendVerificationEmailOtp(request.getTransactionId());
		emailVerifyTxnIdOTP.setOpType(OperationTypeEmail.REGISTRATION);
		// Send the Verification Mail with OTP
		TransactionResponse response = sentEmailVerificationOTP(emailVerifyTxnIdOTP);
		return Objects.nonNull(response.getTransactionId()) ? true : false;
	}

	private String populateGreetingMessage(String name) {
		String WELCOME = "WELCOME ";
		return WELCOME.concat(StringUtils.isNotBlank(name) ? name : "");
	}

}
