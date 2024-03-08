/**
 * 
 */
package com.parserlabs.phr.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.exception.MobileNotVerifiedException;
import com.parserlabs.phr.exception.TransactionNotFoundException;
import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.model.request.RegistrationByMobileOrEmailRequest;
import com.parserlabs.phr.service.LocationService;

/**
 * @author Rajesh
 *
 */
@Component
public class DataValidator {

	@Autowired
	private LocationService locationService;

	/**
	 * 
	 * @param phrTransactionEntity
	 * @param request
	 */
	public void registrationData(PhrTransactionEntity phrTransactionEntity, RegistrationByMobileOrEmailRequest request) {
		if (!phrTransactionEntity.getMobileVerified() && !phrTransactionEntity.getEmailVerified()) {
			throw new TransactionNotFoundException(
					String.format("Transaction is not found for %s UUID", phrTransactionEntity.getTransactionId()));
		}
		String Mobile = StringUtils.isNotBlank(DecryptRSAUtil.decrypt(request.getMobile()))
				? DecryptRSAUtil.decrypt(request.getMobile())
				: request.getMobile();
		if (phrTransactionEntity.getMobileVerified() && !Mobile.equals(phrTransactionEntity.getMobile())) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("value").value(Mobile).build();
			throw new MobileNotVerifiedException("Mobile number is not verifed.", attribute);
		}
		String Email = StringUtils.isNotBlank(DecryptRSAUtil.decrypt(request.getEmail()))
				? DecryptRSAUtil.decrypt(request.getEmail())
				: request.getEmail();
		if (phrTransactionEntity.getEmailVerified() && !Email.equals(phrTransactionEntity.getEmail())) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("value").value(Email).build();
			throw new MobileNotVerifiedException("Email address is not verifed.", attribute);
		}
		// validate the districtCode
		if( !StringUtils.isEmpty(request.getStateCode()) )
		{	
		 locationService.getDistrictName(request.getStateCode(), request.getDistrictCode());
		}
	}	

}
