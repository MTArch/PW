package com.parserlabs.phr.kafkalistener;

import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.model.CreatePHRPayloadRequest;
import com.parserlabs.phr.model.request.CreatePHRFromMobileRequest;
import com.parserlabs.phr.service.RegistrationByMobileOrEmailService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class PhrAddressListener {

	private final RegistrationByMobileOrEmailService registrationService;

	@KafkaListener(topics = "${health.id.phraddress.topic}", groupId = "${health.id.phraddress.topic.groupId}", concurrency = "${health.id.phraddress.topic.concurrency}", containerFactory = "phrAddressListenerContainerFactory")
	private void createPhrAddress(CreatePHRPayloadRequest requestPHR, Acknowledgment acknowledgment) {
		try {
			log.info("PHR Payload payload received in system for ABHA NUMBER {}, ABHA ADDRESS {}",
					requestPHR.getAbhaNumber(), requestPHR.getAbhaAddress());
			CreatePHRFromMobileRequest createPhrMobileRequest = CreatePHRFromMobileRequest.builder().build();
			BeanUtils.copyProperties(requestPHR, createPhrMobileRequest);
			registrationService.createPHRUsingKafka(createPhrMobileRequest);
			acknowledgment.acknowledge();
		} catch (Exception exe) {
			log.info("Error while processing in @KafkaListener ABHA NUMBER {}, ABHA ADDRESS {}",
					requestPHR.getAbhaNumber(), requestPHR.getAbhaAddress());
			log.error("Exception : {}", exe);
		}

	}
}
