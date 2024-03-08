package com.parserlabs.phr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaConfigProperties {

	private String bootstrapServer;
	private String maxPollRecord;
	private String offsetReset;

}
