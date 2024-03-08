package com.parserlabs.phr.model.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.Uuid;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedHidPlayLoad {
	

	@NotBlank
	@ApiModelProperty(example = "iZdmIQNgNY4Vc5NjGirJYMH2/8Thh6GCIumygD9HXOfIOTk/KzhSYIA9+wE7pe4ehA2ThRjllrb1IEXx1XwB4Rrm1McBLSC0z838TSsKBYDQI0RD7cybYNQC4oiB/QNOJ6exvuCv1qCiHDWTfufwJwd7HQRTxGBvtSLZI8cgWrGLqe/kGyvWYzBgewFS+8RZbxwyWfA0jdt28HT5uFycHPCyLsDf2HhlM8f3pXr4ce/qo2NNYCOfhGGIujffVpUum3gYJ1ZRuSMIUqh3Q0BE/950pFHFk5yjsmqJg+xNSwW9YDqGs32Bz533nw+OfmgC0TkU8DNwNTXMjlqjl/ZKyHFHT99eH1yAuAdhi4xyz9qPuELIO/UpStugNV5AcgZ6RNdPhMmNNx0/AyPvq6Dy0VPn5ptk8a4KlNVcDZS4BjvRm1zE/QiTck7d1iZTF4L3OP+d3KaFb16avSp6D5p9TEa2buH+CxMdIUMbD6NUGR6EuUMY4beyJbnO+ytuwmJ+xas47FeeNOwCv08i8G2cXAuEVhGVSQFBHFUS+uzqJXbAmsuAvUcR6Wpoo6tF0zrxYmDNTZlC9UsbUjLAOM+08vWgaP1a6YroMF1pvFPcSyKH942jLJgVNYpH/flpC7XK7ds+FO2K4I27X3Mmliaqg10i2pkmcVGWZr9REmDBmec=",
		dataType="String",name="authToken",required = true)
	private String authToken;
	
	
	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83", dataType = "String", name = "txnId", value = "Based on UUID", required = true)
	@Uuid
	private String transactionId;

}
