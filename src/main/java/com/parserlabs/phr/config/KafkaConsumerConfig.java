package com.parserlabs.phr.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.parserlabs.phr.model.CreatePHRPayloadRequest;
@Configuration
public class KafkaConsumerConfig {

	@Autowired
	private KafkaConfigProperties kafkaConfigProperties;

	private static final String TRUSTED_PACKAGE_PARSERLB = "com.parserlabs.*";

	@Bean
	public Map<String, Object> consumerConfig() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServer());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConfigProperties.getMaxPollRecord());
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConfigProperties.getOffsetReset());
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
		props.put(ConsumerConfig.EXCLUDE_INTERNAL_TOPICS_CONFIG, Boolean.TRUE);
		props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 90000);
		props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 600);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, TRUSTED_PACKAGE_PARSERLB);
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000"); // 30 seconds
	    props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "5000"); // 1 second
	    props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "600000"); // 10 minutes
		return props;
	}


	@Bean
	public ConsumerFactory<String, CreatePHRPayloadRequest> phrAddressConsumerFactory() {
		JsonDeserializer<CreatePHRPayloadRequest> phrAddresssPayloadDeserializer = new JsonDeserializer<>(
				CreatePHRPayloadRequest.class, false);
		phrAddresssPayloadDeserializer.addTrustedPackages(TRUSTED_PACKAGE_PARSERLB);
		return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
				phrAddresssPayloadDeserializer);
	}

	@Bean("phrAddressListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, CreatePHRPayloadRequest> phrAddressListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, CreatePHRPayloadRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(phrAddressConsumerFactory());
		factory.setBatchListener(true);
		factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
		return factory;
	}
}
