package com.parserlabs.phr.adapter.serivce;

import org.springframework.stereotype.Component;

import com.parserlabs.phr.model.request.CreatePHRFromMobileRequest;
import com.parserlabs.phr.proxy.HealthIdProxy;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class HIDDataInsAdapterProxy {

	HealthIdProxy healthIdProxy;

	public CreatePHRFromMobileRequest featchHID(String phrAddress) {
		return healthIdProxy.getAbhaAddressDataUsingPhr(phrAddress);

	}
}
