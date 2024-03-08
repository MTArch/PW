/**
 * 
 */
package com.parserlabs.phr.model.email;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rajesh
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailContentModel {
	private Map<String, Object> templateProps;
	private String templateContent;

	private String toAddress;
	String fromAddress;
	String senderName;
	String subject;
}
