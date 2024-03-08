package com.parserlabs.phr.model.authentication;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class ValidateTokenRequest implements Serializable {
	private static final long serialVersionUID = -5647046235857584058L;
	
	@NotBlank
	@ApiModelProperty(example = "iZdmIQNgNY4Vc5NjGirJYMH2/8Thh6GCIumygD9HXOfIOTk/KzhSYIA9+wE7pe4ehA2ThRjllrb1IEXx1XwB4Rrm1McBLSC0z838TSsKBYDQI0RD7cybYNQC4oiB/QNOJ6exvuCv1qCiHDWTfufwJwd7HQRTxGBvtSLZI8cgWrGLqe/kGyvWYzBgewFS+8RZbxwyWfA0jdt28HT5uFycHPCyLsDf2HhlM8f3pXr4ce/qo2NNYCOfhGGIujffVpUum3gYJ1ZRuSMIUqh3Q0BE/950pFHFk5yjsmqJg+xNSwW9YDqGs32Bz533nw+OfmgC0TkU8DNwNTXMjlqjl/ZKyHFHT99eH1yAuAdhi4xyz9qPuELIO/UpStugNV5AcgZ6RNdPhMmNNx0/AyPvq6Dy0VPn5ptk8a4KlNVcDZS4BjvRm1zE/QiTck7d1iZTF4L3OP+d3KaFb16avSp6D5p9TEa2buH+CxMdIUMbD6NUGR6EuUMY4beyJbnO+ytuwmJ+xas47FeeNOwCv08i8G2cXAuEVhGVSQFBHFUS+uzqJXbAmsuAvUcR6Wpoo6tF0zrxYmDNTZlC9UsbUjLAOM+08vWgaP1a6YroMF1pvFPcSyKH942jLJgVNYpH/flpC7XK7ds+FO2K4I27X3Mmliaqg10i2pkmcVGWZr9REmDBmec=",
		dataType="String",name="authToken",required = true)
	private String authToken;
	
	//need default constructor for JSON Parsing
	public ValidateTokenRequest()
	{
		
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

}