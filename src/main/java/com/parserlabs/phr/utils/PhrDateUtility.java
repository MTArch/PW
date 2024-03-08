/**
 * 
 */
package com.parserlabs.phr.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@Slf4j
@UtilityClass
public class PhrDateUtility {
	public Boolean checkExpiry(long createdTime, long expireTimeMin) {
		Boolean isExpire = false;
		long optRistricTime = 1 * expireTimeMin * 60 ; // [hrs * minutes * seconds]
		if (Objects.nonNull(createdTime)) {
			long currentTime = GeneralUtils.getCurrentTime();
			long second = TimeUnit.MILLISECONDS.toSeconds(currentTime - createdTime);
			isExpire = optRistricTime < second;
			log.info("Differnece in min:: {} ", second / 60);

		}
		return isExpire;

	}

	public Boolean checkPasswordLoginReqExpiry(long createdTime, long expireTimeSec) {
		Boolean isExpired = false;
		if (Objects.nonNull(createdTime)) {
			long currentTime = GeneralUtils.getCurrentTime();
			long diffInSecond = TimeUnit.MILLISECONDS.toSeconds(currentTime - createdTime);
			// convert difference in seconds
			long optRistricTime = diffInSecond;

			log.info("Differnece in Seconds:: {} ", optRistricTime);
			isExpired = expireTimeSec < diffInSecond;
		}
		return isExpired;

	}

	public String getCurrentTimeStamp() {
		SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
		return date.format(new Date());
	}

}
