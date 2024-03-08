package com.parserlabs.phr.model.registration;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.Encryption;
import com.parserlabs.phr.annotation.Otp;
import com.parserlabs.phr.annotation.Uuid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
@ToString
public class VerifyOTPRequest implements Serializable {

	private static final long serialVersionUID = -5377985036801261104L;

	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83", dataType = "String", name = "txnId", value = "Based on UUID", required = true)
	@Uuid
	private String transactionId;
	
	
	@ApiModelProperty(example = "tSCaVUjHwHiMVCokz7u3ogfop5r7ON5GmVY4rJNaQhoVAMlZl5lDqbb4vobfFMsQ1zO404gkWqPqLoDCdavx+JJ5pxprDpRo+PbeV44q51xr5OoNW2ITy9x6WM81KF9o7OnIU3FOGg09jqcJ/By3S8ICWxzJDKVwCJPehHtjhSFiy+mdWEjKkBTrEWJRTy3ZOkij+fskm+JjLoJlIF0TmA94Jb/avX0/LrnacpWEYWAHd0R/8/HIeITVNwG5hnsuRyIcIKKy7bEuYul8wJDD8RPBhL/gIAV4c5zDCb518o1MJGQtNg8Yf/zcROdaynWrBHIh2tacPrxmLHiZHD+BHQ==", dataType = "String", name = "otp", value = "Encrypted Mobile OTP.", required = true)
	@Otp(required = true)
	@Encryption(required = true)
	private String otp;

}