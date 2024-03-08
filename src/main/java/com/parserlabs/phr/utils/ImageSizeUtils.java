/**
 * 
 */
package com.parserlabs.phr.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import com.parserlabs.phr.enums.ImageTypeEnum;
import com.parserlabs.phr.exception.ImageValidationException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@Slf4j
@Service
public class ImageSizeUtils {

	private static final Long PROFILE_IMAGE_FILE_SIZE_MAX_LIMIT = 150000L;
	private static final String[] PROFILE_IMAGE_ALLOWED_IMAGE_EXTENSION = { "png", "jpeg", "jpg" };

	private static final Long DOCUMENT_ID_IMAGE_FILE_SIZE_MAX_LIMIT = 150000L;
	private static final String[] DOCUMENT_ID_ALLOWED_IMAGE_EXTENSION = { "png", "jpeg", "jpg", "pdf" };

	public static boolean imageValidation(String value, ImageTypeEnum imageType) {
		return imageValidation(value.getBytes(), imageType);
	}

	public static boolean imageValidation(byte[] value, ImageTypeEnum imageType) {
		
		long IMAGE_FILE_SIZE_MAX_LIMIT = getAllowedSize(imageType);
		String[] ALLOWED_IMAGE_EXTENSION = getAllowedExtensions(imageType);

		boolean isValid = true;
		byte[] decodedBytes = value;
		log.info("Other ID Doc size of image received : {} KB", decodedBytes.length/1000);
		if (decodedBytes.length > IMAGE_FILE_SIZE_MAX_LIMIT) {
			throw new ImageValidationException(
					"$$Document file size is invalid. Image size should not be greater than "+ IMAGE_FILE_SIZE_MAX_LIMIT/1000 +" KB");
		}

		String contentType = getImageContentType(decodedBytes);
		String imageExtension = Arrays.stream(contentType.split("/")).collect(Collectors.toList()).get(1);
		log.info("Other ID Doc size of image extension type : {} ", imageExtension);
		List<String> imageExtensions = Arrays.asList(ALLOWED_IMAGE_EXTENSION);
		if (!imageExtensions.contains(imageExtension)) {
			throw new ImageValidationException("$$Please upload a valid format document file image "+ imageExtensions.toString() +" file");
		}

		return isValid;
	}

	private static long getAllowedSize(ImageTypeEnum imageType) {
		switch (imageType) {
		case PROFILE_IMAGE:
			return PROFILE_IMAGE_FILE_SIZE_MAX_LIMIT;
		case DOCOMENT_ID_IMAGE:
			return DOCUMENT_ID_IMAGE_FILE_SIZE_MAX_LIMIT;
		default:
			return 0;
		}
	}
	
	private static String[] getAllowedExtensions(ImageTypeEnum imageType) {
		switch (imageType) {
		case PROFILE_IMAGE:
			return PROFILE_IMAGE_ALLOWED_IMAGE_EXTENSION;
		case DOCOMENT_ID_IMAGE:
			return DOCUMENT_ID_ALLOWED_IMAGE_EXTENSION;
		default:
			return null;
		}
	}

	public static String getImageContentType(byte[] value) {
		try {
			return new Tika().detect(value);
		} catch (Exception e) {
			return null;
		}
	}

}
