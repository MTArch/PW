package com.parserlabs.phr.adapter.serivce;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.parserlabs.phr.model.adapter.response.HidResponse;
import com.parserlabs.phr.proxy.HealthIdProxy;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class HidRegistrationProxy {

	HealthIdProxy HealthIdProxy;

	public HidResponse featchUserProfile() {
		return HealthIdProxy.fetchUserProfile();

	}
	
	public List<String> getPHRFromHID(Set<String> abhaList){
		return HealthIdProxy.getExistAbhaList(abhaList);
	}

}
