package com.parserlabs.phr.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.parserlabs.phr.model.notification.MailPayload;
import com.parserlabs.phr.model.notification.SmsPayLoad;


@Configuration
@ConditionalOnExpression("${kafka.service.enabled:true}")
public class KafkaProducerConfig {

	public static final int BATCH_SIZE_CONFIG = 100;
	public static final int LINGER_MS = 50;
	public static final int REQUEST_TIMEOUT_MS = 150000;

	@Value("${spring.kafka.producer.bootstrapServer}")
	private String producerBootstrapServer;

	@Value("${phr.id.sms.topic}")
	private String smsTopic;
	
	@Value("${phr.id.email.topic}")
	private String emailTopic;
	
	

	@Bean
	public KafkaAdmin kafkaAdmin() {
		return new KafkaAdmin(producerConfigs());
	}

	@Bean("producerConfigs")
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServer);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, REQUEST_TIMEOUT_MS);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, BATCH_SIZE_CONFIG);
		props.put(ProducerConfig.LINGER_MS_CONFIG, LINGER_MS);
		props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 600);
		props.put(ProducerConfig.ACKS_CONFIG, "1");
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return props;
	}

	@Bean(name = "smsKafkaTemplate")
	public KafkaTemplate<String, SmsPayLoad> smsKafkaTemplate() {
		return new KafkaTemplate<>(smsProducerFactory());
	}

	@Bean(name = "smsProducerFactory")
	public ProducerFactory<String, SmsPayLoad> smsProducerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public NewTopic smsTopic() {
		return new NewTopic(smsTopic, 1, (short) 1);
	}
	
	// email
	@Bean(name = "emailKafkaTemplate")
	public KafkaTemplate<String, MailPayload> emailKafkaTemplate() {
		return new KafkaTemplate<>(emailProducerFactory());
	}

	@Bean(name = "emailProducerFactory")
	public ProducerFactory<String, MailPayload> emailProducerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public NewTopic emailTopic() {
		return new NewTopic(emailTopic, 1, (short) 1);
	}
}

