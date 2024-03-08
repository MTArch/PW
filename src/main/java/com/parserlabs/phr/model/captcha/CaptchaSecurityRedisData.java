package com.parserlabs.phr.model.captcha;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.parserlabs.phr.enums.CaptchaStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
public class CaptchaSecurityRedisData {
	private Long id;
	protected UUID captchaId;
	private String clientId;
	private String origin;
	private String clientIp;
	private String latitude;
	private String longitude;
	private String captchaText;
	private String captchaAnswer;
	private String captchAction;
	private boolean captchaValid;
	private CaptchaStatus status;
    private int retryCount;
	@CreatedDate
	private Date createdDate;
	@LastModifiedDate
	private Date updateDate;
	private String lastUpdatedBy;
}
