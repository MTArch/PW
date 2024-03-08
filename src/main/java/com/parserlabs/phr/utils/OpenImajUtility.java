/**
 * 
 */
package com.parserlabs.phr.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import com.parserlabs.phr.model.face.FaceResponse;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@UtilityClass
@Slf4j
public class OpenImajUtility {

	public FaceResponse isFaceFoundProfile(Object image, int haarCascadeSizeValue) {
		if (Objects.isNull(image) || image.toString().length() == 0) {
			return FaceResponse.builder().faceFound(false).noOfFaces(0).build();
		}
		log.info("Face size : {}", image.toString().length());
		byte[] decodedImageBytes = null;
		if (image instanceof String) {
			decodedImageBytes = Base64.getDecoder().decode(((String) image).getBytes());
		} else if (image instanceof byte[]) {
			decodedImageBytes = (byte[]) image;
		}
		InputStream stream = new ByteArrayInputStream(decodedImageBytes);
		log.info("haarCascadeSizeValue : {}", haarCascadeSizeValue);
		return isFaceFound(stream, haarCascadeSizeValue);
	}

	private FaceResponse isFaceFound(InputStream stream, int haarCascadeSize) {
		boolean isFaces = false;
		// Convert Base64 to MBFImage
		MBFImage image;
		int faces = 0;
		try {
			image = ImageUtilities.readMBF(stream);
			faces = detectFace(image, haarCascadeSize);
			log.info("No of Faces in Photo: {}", faces);
			isFaces = faces == 1;
		} catch (IOException e) {
			log.error("Face detector exp : {}", e);
			isFaces = false;
		}
		return FaceResponse.builder().faceFound(isFaces).noOfFaces(faces).build();
	}

	private int detectFace(MBFImage image, int haarCascadeSize) {
		FaceDetector<DetectedFace, FImage> fd = new HaarCascadeDetector(haarCascadeSize);
		List<DetectedFace> faces = fd.detectFaces(Transforms.calculateIntensity(image));
		return faces.size();
	}
}
