/**
 * 
 */
package com.parserlabs.phr.model.notification;

import lombok.Builder;
import lombok.Data;

/**
 * @author Rajesh
 *
 */
@Data
@Builder
public class SmsPayLoad {
	private String phoneNumber;
	private String message;
	private String login;
	private String key;
	private String templeId;
	private String origin;
	private String signature;
	private String entityId;
}
