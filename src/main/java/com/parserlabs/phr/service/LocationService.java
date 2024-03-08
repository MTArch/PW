package com.parserlabs.phr.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.exception.DistrictNotValidException;
import com.parserlabs.phr.exception.StateNotValidException;
import com.parserlabs.phr.model.District;
import com.parserlabs.phr.model.States;
import com.parserlabs.phr.proxy.HealthIdProxy;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@CustomSpanned
public class LocationService {

	private HealthIdProxy healthIdProxy;

	private List<States> stateGlobalList = null;
	private Map<String, List<District>> districtGlobalList = new TreeMap<>();

	public List<States> getStates() {
		if (stateGlobalList.isEmpty()) {
			stateGlobalList = healthIdProxy.getStates();
		}
		return stateGlobalList;
	}

	public List<District> getDistricts(String stateCode) {

		districtGlobalList.computeIfAbsent(stateCode, districtList -> healthIdProxy.getDistrictsInState(stateCode));

		return districtGlobalList.get(stateCode);
	}

	public String getStateName(String stateCode) {
		return getStates().stream().filter(state -> state.getCode().equalsIgnoreCase(stateCode)).findFirst()
				.orElseThrow(StateNotValidException::new).getName();
	}

	public String getDistrictName(String stateCode, String districtCode) {
		return getDistricts(stateCode).stream().filter(district -> district.getCode().equalsIgnoreCase(districtCode))
				.findFirst().orElseThrow(DistrictNotValidException::new).getName();
	}
}
