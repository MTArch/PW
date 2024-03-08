package com.parserlabs.phr.controller.v1.search.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.controller.v1.search.LocationController;
import com.parserlabs.phr.model.District;
import com.parserlabs.phr.model.States;
import com.parserlabs.phr.service.LocationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(LOCATION_V1_ENDPOINT)
@AllArgsConstructor
@CrossOrigin
@CustomSpanned
public class LocationControllerImpl implements LocationController {

	private final LocationService locationService;

	@Override
	@GetMapping("districts")
	public ResponseEntity<List<District>> getDistrictsInState(@RequestParam String stateCode) {
		return ResponseEntity.ok(locationService.getDistricts(stateCode));
	}

	@Override
	@GetMapping(value = "states")
	public ResponseEntity<List<States>> getStates() {
		return ResponseEntity.ok(locationService.getStates());
	}
}
