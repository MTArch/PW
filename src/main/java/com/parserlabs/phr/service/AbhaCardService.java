/**
 * 
 */
package com.parserlabs.phr.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;
import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.constants.CardConstants;
import com.parserlabs.phr.enums.AbhaCardTypeEnums;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.utils.Argon2Encoder;
import com.parserlabs.phr.utils.PHRIdUtils;
import com.parserlabs.phr.utils.PhrCardHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@Service
@Slf4j
@CustomSpanned
public class AbhaCardService {
	@Autowired
	private UserService userService;

	public byte[] getABHACard(String mediaType) {
		return getABHACard(AbhaCardTypeEnums.valueOf(mediaType.toUpperCase()));
	}

	/**
	 * Generate the ABHA CARD
	 * 
	 * @param abhaCardType
	 * @return
	 */
	private byte[] getABHACard(AbhaCardTypeEnums abhaCardType) {
		UserDTO user = userService.get();
		byte[] abhaCardByte = null;

		switch (abhaCardType) {
		case PDF:
			abhaCardByte = PhrCardHelper.getABHACardPDF(user);
			break;
		case PNG:
			abhaCardByte = PhrCardHelper.getABHACardPNG(user);
			break;
		case SVG:
			abhaCardByte = PhrCardHelper.getABHACardSVG(user);
			break;
		case STRING:
			abhaCardByte = getQrCodeOfAccount(user);
			break;
		default:
			break;
		}
		return abhaCardByte;

	}

	private byte[] getQrCodeOfAccount(UserDTO user) {
		return getQrCodeOfAccount(user, true);
	}

	private byte[] getQrCodeOfAccount(UserDTO user, Boolean isQrCodeWithProfileData) {
		byte[] qrCodeByte = null;
		try {
			if (isQrCodeWithProfileData) {
				qrCodeByte = PhrCardHelper.getQrCodeOfUserWithLogo(user);
			} else {
				String saltedValue = Argon2Encoder.encode(user.getPhrAddress(), PHRIdUtils.getSaltedValue(user));
				qrCodeByte = PhrCardHelper.getQrCodeOfUserWithLogo(
						String.format(CardConstants.qrRedirectLink, user.getPhrAddress(), saltedValue));
			}
		} catch (WriterException e) {
			log.error("WriterException occured:", e);
		} catch (IOException e) {
			log.error("IOException occured:", e);
		}
		return qrCodeByte;
	}

}
