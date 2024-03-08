package com.parserlabs.phr.controller.v1.search.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import java.util.Collections;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.controller.v1.search.SearchController;
import com.parserlabs.phr.model.request.SearchRequestPayLoad;
import com.parserlabs.phr.model.response.SearchResponsePayLoad;
import com.parserlabs.phr.service.SearchService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(SEARCH_V1_ENDPOINT)
@AllArgsConstructor
@CrossOrigin
@CustomSpanned
public class SearchControllerImpl implements SearchController{

	private final SearchService searchService;

	@GetMapping("exists/byhealthId/phrAddress/{phrAddress}")
	@Override
	public ResponseEntity<Map<String, Boolean>> doesPhrAddressExists(@PathVariable("phrAddress") String phrAddress) {
		return ResponseEntity.ok(Collections.singletonMap("status", searchService.doesPhrAddressByHealthIdExists(phrAddress)));
	}

	@Override
	public ResponseEntity<SearchResponsePayLoad> SearchByHealthId(@Valid @RequestBody SearchRequestPayLoad searchRequestPayLoad) {
		return ResponseEntity.ok(searchService.featchAuthMethod(searchRequestPayLoad));
	}
	


}
