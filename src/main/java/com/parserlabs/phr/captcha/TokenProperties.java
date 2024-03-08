package com.parserlabs.phr.captcha;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenProperties {
	private boolean valid;
	private String invalidReason;
	private String hostname;
	private String action;
	private String createTime;
	

}
