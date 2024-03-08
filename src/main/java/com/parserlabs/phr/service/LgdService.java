package com.parserlabs.phr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.component.JsonReader;
import com.parserlabs.phr.exception.DistrictNotValidException;
import com.parserlabs.phr.exception.StateNotValidException;
import com.parserlabs.phr.model.lgd.DataModel;
import com.parserlabs.phr.model.lgd.DistrictDTO;
import com.parserlabs.phr.model.lgd.StatesDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@CustomSpanned
public class LgdService {

	private static final Map<String, StatesDTO> STATE_CODE_MAP = new TreeMap<String, StatesDTO>();
	private static final Map<String, StatesDTO> STATE_NAME_MAP = new TreeMap<String, StatesDTO>();
	private static final Set<StatesDTO> STATES = new TreeSet<>();

	@SuppressWarnings({ "unused", "serial" })
	private static final Map<String, String> RENAMED_STATES = new HashMap<String, String>() {{
		put("Andaman & Nicobar Islands".toUpperCase(), "ANDAMAN AND NICOBAR ISLANDS");
	    put("Andaman and Nicobar Islands".toUpperCase(), "ANDAMAN AND NICOBAR ISLANDS");
	    put("Jammu & Kashmir".toUpperCase(), "JAMMU AND KASHMIR");
	    put("Dadra and Nagar Haveli".toUpperCase(), "THE DADRA AND NAGAR HAVELI AND DAMAN AND DIU");
	    put("Dadra & Nagar Haveli".toUpperCase(), "THE DADRA AND NAGAR HAVELI AND DAMAN AND DIU");
	    put("Daman and Diu".toUpperCase(), "THE DADRA AND NAGAR HAVELI AND DAMAN AND DIU");
	    put("Daman & Diu".toUpperCase(), "THE DADRA AND NAGAR HAVELI AND DAMAN AND DIU");
	    put("Westbengal".toUpperCase(), "WEST BENGAL");
	    put("PONDICHERRY".toUpperCase(), "PUDUCHERRY");
	}};
	
	// TODO get this from property file
	private static final String LGD_STATES_JSON_FILE = "lgd_latest.json";
	private static ObjectMapper mapper = new ObjectMapper();

//	@Autowired
	private JsonReader jsonReader;

//	@Value("${lgd_states_json_file}")
//	private String lgdStatesJsonFile;

	public LgdService(JsonReader jsonReader) {
		this.jsonReader = jsonReader;
		// Loading state data
		loadStateData();
	}

	public List<StatesDTO> getStates() {
		return new ArrayList<StatesDTO>(STATES);
	}

	public Boolean isValidState(String key) {
		//IMP: Trim it before use.
		key = key.trim();
		
		boolean isValidState = false;
		if (StringUtils.hasText(key)
				&& (STATE_CODE_MAP.containsKey(key) || STATE_NAME_MAP.containsKey(key.toUpperCase()))) {
			isValidState = true;
		}

		if (!isValidState) {
			throw new StateNotValidException(String.format("enter valid state code: %s", key));
		}
		return isValidState;
	}

	public StatesDTO getState(String key) {
		//IMP: Trim it before use.
		key = key.trim();
		StatesDTO state = null;
		
		if (isValidState(key)) {
			if (STATE_CODE_MAP.containsKey(key)) {
				state = STATE_CODE_MAP.get(key);
			} else if (STATE_NAME_MAP.containsKey(key.toUpperCase())) {
				state = STATE_NAME_MAP.get(key.toUpperCase());
			}
		}
		return state;
	}
	
	/**
	 * Get StatesDTO by state code or name if available
	 * @param key code or name of state
	 * @return StatesDTO or null if not available.
	 */
	public StatesDTO getStateByCodeOrName(String key) {
		
		Optional<StatesDTO> state = Optional.ofNullable(null);
		if(StringUtils.hasText(key)) {
			//IMP: Trim it before use.
			key = key.trim();
			if (STATE_CODE_MAP.containsKey(key)) {
				state = Optional.ofNullable(STATE_CODE_MAP.get(key));
			} else if (STATE_NAME_MAP.containsKey(key.toUpperCase())) {
				state = Optional.ofNullable(STATE_NAME_MAP.get(key.toUpperCase()));
			}
		}
		return state.isPresent() ? state.get() : null;
	}
	
	/**
	 * Get DistrictDTO by state code or name if available
	 * @param stateKey code or name of state
	 * @param districtKey code or name of district
	 * @return DistrictDTO or null if not available.
	 */
	public DistrictDTO getDistrictByCodeOrName(String stateKey, String districtKey) {
		//IMP: Trim it before use.
		stateKey = stateKey.trim();
		Optional<DistrictDTO> optionalFindFirst = Optional.ofNullable(null);
		StatesDTO state = getStateByCodeOrName(stateKey);
		if (Objects.nonNull(state)) {
			optionalFindFirst = state.getDistricts().stream()
					.filter(distract -> DistrictDTO.isValidKey(districtKey.trim(), distract.getCode(), distract.getName()))
					.findFirst();
		}
		return optionalFindFirst.isPresent() ? optionalFindFirst.get() : null;
	}

	public List<DistrictDTO> getDistrictFromState(String key) {
		return isValidState(key) ? getState(key).getDistricts() : null;
	}

	public DistrictDTO getDistrict(String stateKey, String districtKey, boolean isOptional) {
		Optional<DistrictDTO> optionalFindFirst = null;
		if (isValidState(stateKey)) {
			StatesDTO state = getState(stateKey);
			if (Objects.nonNull(state)) {
				optionalFindFirst = state.getDistricts().stream()
						.filter(distract -> DistrictDTO.isValidKey(districtKey, distract.getCode(), distract.getName()))
						.findFirst();
			}

			if (!optionalFindFirst.isPresent() && !isOptional) {
				throw new DistrictNotValidException(String.format("Enter valid district %s", districtKey));
			}
		}
		return optionalFindFirst.isPresent() ? optionalFindFirst.get() : null;

	}

	public String getDistrictName(String stateCode, String distractCode, boolean source) {
		DistrictDTO district = getDistrict(stateCode, distractCode, source);
		return Objects.nonNull(district) ? district.getName() : "";
	}

	public String getStateName(String stateCode) {
		StatesDTO state = getState(stateCode);
		return Objects.nonNull(state) ? state.getName() : "";
	}

	private List<DataModel> jsonParser(String filename) {
		List<DataModel> stateDataModelList = null;
		try {
			stateDataModelList = Arrays.asList(mapper.readValue(jsonReader.jsonReader(filename), DataModel[].class));
		} catch (Exception e) {
			log.error("Exception occured while reading/parsing the Json file ", e);
		}
		return stateDataModelList;
	}

	/**
	 * Load Master data of State/District from LGD json file into hashmap. 
	 */
	private void loadStateData() {
		List<DataModel> stateDataModelList = jsonParser(LGD_STATES_JSON_FILE);
		if (!CollectionUtils.isEmpty(stateDataModelList)) {
			stateDataModelList.stream().forEach(stateData -> {
				if (!STATE_CODE_MAP.containsKey(stateData.getStateCode())) {
					StatesDTO state = StatesDTO.of(stateData.getStateCode().trim(), 
							stateData.getStateName().trim().toUpperCase());
					STATE_CODE_MAP.put(stateData.getStateCode(), state);
					STATE_NAME_MAP.put(stateData.getStateName(), state);
				}
				DistrictDTO district = DistrictDTO.of(stateData.getDistrictCode().trim(), 
						stateData.getDistrictName().trim().toUpperCase());
				STATE_CODE_MAP.get(stateData.getStateCode()).getDistricts().add(district);				
			});
			STATES.addAll(STATE_CODE_MAP.values().stream().collect(Collectors.toList()));
		}
	}
}
