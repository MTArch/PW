package com.parserlabs.phr.adapter.serivce;

import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrAuthTransactionEntity;
import com.parserlabs.phr.enums.AccountAction;
import com.parserlabs.phr.enums.AuthStatusEnums;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.model.adapter.request.ConfrimLoginRequest;
import com.parserlabs.phr.model.adapter.request.TransactionInitRequestPayLoad;
import com.parserlabs.phr.model.adapter.response.HidTransaction;
import com.parserlabs.phr.model.registration.ResendLoginOTPRequest;
import com.parserlabs.phr.model.request.AuthIntRequestPayLoad;
import com.parserlabs.phr.model.request.LoginRequestPayload;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.proxy.HealthIdProxy;
import com.parserlabs.phr.proxy.model.PhrDeLinkedRequestPayLoad;
import com.parserlabs.phr.proxy.model.PhrLinkedOrDeLinkedRequestPayLoad;
import com.parserlabs.phr.utils.GeneralUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suraj
 *
 */
@Slf4j
@Component
@AllArgsConstructor
@CustomSpanned
public class AuthAdapterProxy {

	private final HealthIdProxy healthIdProxy;

	public PhrAuthTransactionEntity processAuthInitiate(AuthIntRequestPayLoad authIntRequestPayLoad) {
		TransactionInitRequestPayLoad transactionInitRequestPayLoad = TransactionInitRequestPayLoad.builder()
				.authMethod(authIntRequestPayLoad.getAuthMethod()).healthid(authIntRequestPayLoad.getHealhtIdNumber())
				.build();
		HidTransaction transaction = healthIdProxy.authInitiate(transactionInitRequestPayLoad);

		return PhrAuthTransactionEntity.builder().healthIdTransaction(transaction.getTxnId())
				.status(AuthStatusEnums.PRE_OTP.name()).healthIdNumber(authIntRequestPayLoad.getHealhtIdNumber())
				.authMethod(authIntRequestPayLoad.getAuthMethod()).build();

	}

	
	public PhrAuthTransactionEntity processResendOtp(ResendLoginOTPRequest resendOTPRequest,PhrAuthTransactionEntity phrAuthTransactionEntity) {
	
		//Re-send OTP
		healthIdProxy.resendOpt(resendOTPRequest);
		return PhrAuthTransactionEntity.builder().healthIdTransaction(phrAuthTransactionEntity.getHealthIdTransaction())
				.status(AuthStatusEnums.PRE_OTP.name()).healthIdNumber(phrAuthTransactionEntity.getHealthIdNumber())
				.authMethod(phrAuthTransactionEntity.getAuthMethod()).build();
	}

	
	
	public JwtResponse confirmLogin(LoginRequestPayload loginRequest) {
		return healthIdProxy.confirmLogin(ConfrimLoginRequest
				.builder().txnId(loginRequest.getTransactionId()).value(DecryptRSAUtil
						.encrypt(DecryptRSAUtil.decrypt(loginRequest.getValue()), healthIdProxy.fetchCert().getBody()))
				.build());

	}

	//@Async
	public Map<String,Boolean> linkageProcess(String action, String phrAddress,String healthIdNumber) {

		PhrLinkedOrDeLinkedRequestPayLoad phrLinkedOrDeLinkedRequestPayLoad = null;
		Map<String,Boolean> responseCode = null ; 
		
			if (AccountAction.LINK.name().equals(action)) {
				phrLinkedOrDeLinkedRequestPayLoad = PhrLinkedOrDeLinkedRequestPayLoad.builder()
						.phrAddress(GeneralUtils.deSanetizePhrAddress(phrAddress))
						.preferred(true).build();
				try {
			          responseCode = healthIdProxy.linked(phrLinkedOrDeLinkedRequestPayLoad);
			      } catch (Exception e) {
	                 log.info("Error while calling {} from HID for phrAddress {}",action,phrAddress, e);
	                 responseCode = new HashMap<String, Boolean>();
	                 responseCode.put("status",false);
			      }

			} else if (AccountAction.DELINK.name().equals(action)) {
				PhrDeLinkedRequestPayLoad phrDeLinkedRequestPayLoad = PhrDeLinkedRequestPayLoad.builder()
						                                                         .phrAddress(phrAddress)
						                                                         .healthIdNumber(healthIdNumber)
						                                                         .build();
				responseCode = healthIdProxy.delink(phrDeLinkedRequestPayLoad);
			}
          return responseCode;
	}


}
