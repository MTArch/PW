package com.parserlabs.phr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.model.notification.Mail;
import com.parserlabs.phr.model.notification.MailPayload;
import com.parserlabs.phr.model.notification.SmsPayLoad;


/**
 * @author Rajesh
 * <h1>KAFKA Service to send the SMS and Mail Notification and OTP</h1>
 * */
@Component
@CustomSpanned
public class KafkaService {

	private static final String ORIGIN_HEALTH_ID = "HEALTH_ID";

	@Autowired
	private KafkaTemplate<String, SmsPayLoad> smsKafkaTemplate;

	@Autowired
	private KafkaTemplate<String, MailPayload> emailKafkaTemplate;

	@Value("${phr.id.sms.topic}")
	private String smsTopic;

	@Value("${phr.id.email.topic}")
	private String emailTopic;

	@Async
	public void send(String phoneNumber, String message, String login, String pw, String templeId, String entityId,
			String signature) {
		sendSms(smsPayload(phoneNumber, message, login, pw, templeId, entityId, signature));
	}

	public void sendSms(SmsPayLoad smsPayload) {
		Message<SmsPayLoad> message = MessageBuilder.withPayload(smsPayload).setHeader(KafkaHeaders.TOPIC, smsTopic)
				.build();
		smsKafkaTemplate.send(message);
	}

	private SmsPayLoad smsPayload(String phoneNumber, String message, String login, String pw, String templeId,
			String entityId, String signature) {
		return SmsPayLoad.builder().entityId(entityId).login(login).key(pw).phoneNumber(phoneNumber).message(message)
				.templeId(templeId).origin(ORIGIN_HEALTH_ID).signature(signature).build();

	}

	/**
	 * KAFKA message producer for the Listener to send email
	 * 
	 * @param payload
	 */
	public void sendEmail(Mail mail) {
		MailPayload payload = MailPayload.builder().from(mail.getFrom()).mailTo(mail.getMailTo())
				.subject(mail.getSubject()).senderName(mail.getSenderName()).mailContent(mail.getMailContent())
				.attachments(mail.getAttachments()).origin(ORIGIN_HEALTH_ID).build();
		sendMailProducer(payload);
	}

	
	@Async
	private void sendMailProducer(MailPayload payload) {
		Message<MailPayload> message = MessageBuilder.withPayload(payload).setHeader(KafkaHeaders.TOPIC, emailTopic)
				.build();
		emailKafkaTemplate.send(message);
	}
}
