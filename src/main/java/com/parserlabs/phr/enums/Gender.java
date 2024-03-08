package com.parserlabs.phr.enums;

public enum Gender {
    FEMALE("Female", "F"), MALE("Male", "M"), OTHERS("Others", "O"), UNDISCLOSED("Undisclosed","U");
    public static Gender byCode(String code) {
    	for(Gender gender: Gender.values()) {
    		if(gender.code.equalsIgnoreCase(code)) {
    			return gender;
    		}
    	}
    	return null;
    }
    public static boolean isValid(String status) {
    	Gender[] values = Gender.values();
    	for(Gender auth : values) {
    		if(auth.toString().equals(status)) {
    			return true;
    		}
    	}
		return false;
    }
    
    public static boolean isValidCode(String code) {
    	Gender[] values = Gender.values();
    	for(Gender gender : values) {
    		if(gender.code.toString().equals(code)) {
    			return true;
    		}
    	}
		return false;
    }
	private String code;
	
	private String name;
    
    private Gender(String name, String code) {
		this.name = name;
		this.code= code;
	}
    
    public String getCode() {
    	return this.code;
    }
    
    @Override
    public String toString() {
    	return this.name;
    }
}
