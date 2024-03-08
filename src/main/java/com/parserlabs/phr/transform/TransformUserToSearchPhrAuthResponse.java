package com.parserlabs.phr.transform;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.search.SearchPhrAuthResponse;

@Component
@CustomSpanned
public class TransformUserToSearchPhrAuthResponse implements Function<UserDTO, SearchPhrAuthResponse>{

	@Override
	public SearchPhrAuthResponse apply(UserDTO user) {
		return SearchPhrAuthResponse.builder().phrAddress(user.getPhrAddress()).authMethods(user.getAuthMode()).build();
	}
	
	
	
}
