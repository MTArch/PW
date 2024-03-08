/**
 * 
 */
package com.parserlabs.phr.model.notification;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rajesh
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail {

	private String from;
	private String mailTo;
	private String subject;
	private String senderName;
	private String mailContent;
	private List<Object> attachments;
	private Map<String, Object> props;
}
