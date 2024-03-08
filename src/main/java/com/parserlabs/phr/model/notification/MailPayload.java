/**
 * 
 */
package com.parserlabs.phr.model.notification;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author Rajesh
 * @Date 06-12-2021
 *
 */
@Data
@Builder
public class MailPayload {
	private String from;
	private String mailTo;
	private String subject;
	private String senderName;
	private String mailContent;
	private String origin;

	private List<Object> attachments;

}
