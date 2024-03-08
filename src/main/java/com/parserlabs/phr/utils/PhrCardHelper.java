package com.parserlabs.phr.utils;

import static com.parserlabs.phr.constants.Constants.ADDRESS;
import static com.parserlabs.phr.constants.Constants.GENDER;
import static com.parserlabs.phr.constants.Constants.NAME;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.fop.svg.PDFTranscoder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGImageElement;

import com.google.gson.JsonObject;
import com.google.zxing.WriterException;
import com.parserlabs.phr.constants.CardConstants;
import com.parserlabs.phr.enums.Gender;
import com.parserlabs.phr.exception.SystemException;
import com.parserlabs.phr.model.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PhrCardHelper {

	private static final String PLACEHOLDER_FOR_EMPTY = "-";
	private static final int RESOLUTION_DPI = 400;
	// Image needs to be scaled if DPI changed from default 72 DPI. Change should be
	// done in ration of DPI change so calculate according.
	private static float SCALE_BY_RESULTION = RESOLUTION_DPI / 72f;

	/**
	 * generate the bytearray
	 * 
	 * @param resource
	 * @return
	 */
	public static ByteArrayResource populateByteArray(byte[] resource) {
		return new ByteArrayResource(resource);
	}

	/**
	 * generate QRCode based on string.
	 * 
	 * @param text
	 * @return Base64 encoded image of QR Code.
	 * @throws WriterException
	 * @throws IOException
	 */
	public static byte[] getQrCodeOfUser(UserDTO user) throws WriterException, IOException {
		return QrCode.generateQRCodeImage(userToJson(user));
	}

	/**
	 * generate QRCode with logo based on string.
	 * 
	 * @param text/UserDTO
	 * @return Base64 encoded image of QR Code.
	 * @throws WriterException
	 * @throws IOException
	 */
	public static byte[] getQrCodeOfUserWithLogo(UserDTO user) throws WriterException, IOException {
		return QrCode.generateQRCodeImageWithLogo(userToJson(user));
	}

	public static byte[] getQrCodeOfUserWithLogo(String inputData) throws WriterException, IOException {
		return QrCode.generateQRCodeImageWithLogo(inputData);
	}

	/**
	 * generate QRCode based on string.
	 * 
	 * @param text
	 * @return Base64 encoded image of QR Code.
	 * @throws WriterException
	 * @throws IOException
	 */
	public static String generateQRCodeString(UserDTO user) throws WriterException, IOException {
		return Base64.getEncoder().encodeToString(getQrCodeOfUserWithLogo(user));
	}

	public static String generateQRCodeString(String qrData) throws WriterException, IOException {
		return Base64.getEncoder().encodeToString(getQrCodeOfUserWithLogo(qrData));
	}

	public static byte[] getABHACardPDF(UserDTO user) {
		Transcoder transcoder = new PDFTranscoder();
		TranscoderInput transcoderInput;
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		try {
			Document clonedDoc = loadIdCard();
			if (Objects.nonNull(clonedDoc)) {
				UpdateSVGCard(clonedDoc, user);

				transcoderInput = new TranscoderInput(clonedDoc);
				TranscoderOutput transcoderOutput = new TranscoderOutput(ostream);
				transcoder.transcode(transcoderInput, transcoderOutput);
			} else {
				throw new SystemException("Something went wrong. Please try after sometime");
			}
		} catch (TranscoderException e) {
			log.error("TranscoderException occured:", e);
		} catch (WriterException e) {
			log.error("WriterException occured:", e);
		} catch (IOException e) {
			log.error("IOException occured:", e);
		}

		return ostream.toByteArray();
	}

	public static byte[] getABHACardPNG(UserDTO user) {
		Transcoder transcoder = new PNGTranscoder();
		TranscoderInput transcoderInput;
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		try {
			Document clonedDoc = loadIdCard();
			if (Objects.nonNull(clonedDoc))
				UpdateSVGCard(clonedDoc, user);

			transcoderInput = new TranscoderInput(clonedDoc);
			TranscoderOutput transcoderOutput = new TranscoderOutput(ostream);
			transcoder.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);
			transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, Float.valueOf(252 * SCALE_BY_RESULTION));
			transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, Float.valueOf(144 * SCALE_BY_RESULTION));
			transcoder.addTranscodingHint(PNGTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER,
					Float.valueOf(25.4f / RESOLUTION_DPI));
			transcoder.addTranscodingHint(PNGTranscoder.KEY_MEDIA, "image/png");
			transcoder.transcode(transcoderInput, transcoderOutput);
		} catch (TranscoderException | WriterException | IOException e) {
			log.error("Exception occured:", e);
		}
		return ostream.toByteArray();
	}

	public static byte[] getABHACardSVG(UserDTO user) {
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		try {
			Document clonedDoc = loadIdCard();
			UpdateSVGCard(clonedDoc, user);

			TransformerFactory transformerFactory = PhrUtilits.getSecureTransformFactory();
			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(clonedDoc);
			StreamResult result = new StreamResult(ostream);

			transformer.transform(source, result);
		} catch (WriterException e) {
			log.error("WriterException occured:", e);
		} catch (IOException e) {
			log.error("IOException occured:", e);
		} catch (TransformerException e) {
			log.error("TransformerException occured:", e);
		}
		return ostream.toByteArray();
	}

	private static String userToJson(UserDTO user) {
		JsonObject userData = new JsonObject();
		userData.addProperty("hidn",
				StringUtils.isBlank(user.getHealthIdNumber()) ? PLACEHOLDER_FOR_EMPTY : user.getHealthIdNumber());
		userData.addProperty("phr",
				StringUtils.isBlank(user.getPhrAddress()) ? PLACEHOLDER_FOR_EMPTY : user.getPhrAddress());
		userData.addProperty(NAME, user.getFullName());
		userData.addProperty(GENDER, user.getGender());
		userData.addProperty("statelgd", StringUtils.isBlank(user.getAddress().getStateCode()) ? PLACEHOLDER_FOR_EMPTY
				: user.getAddress().getStateCode());
		userData.addProperty("distlgd", StringUtils.isBlank(user.getAddress().getDistrictCode()) ? PLACEHOLDER_FOR_EMPTY
				: user.getAddress().getDistrictCode());
		userData.addProperty("dob", StringUtils.isNotBlank(user.getDateOfBirth()) ? user.getDateOfBirth()
				: CommonUtils.populateDateOfBirth(user.getDayOfBirth(), user.getMonthOfBirth(), user.getYearOfBirth()));
		userData.addProperty(ADDRESS, StringUtils.isBlank(user.getAddress().getAddressLine()) ? PLACEHOLDER_FOR_EMPTY
				: user.getAddress().getAddressLine());
		userData.addProperty("state name", StringUtils.isBlank(user.getAddress().getStateName()) ? PLACEHOLDER_FOR_EMPTY
				: user.getAddress().getStateName());
		userData.addProperty("dist name",
				StringUtils.isBlank(user.getAddress().getDistrictName()) ? PLACEHOLDER_FOR_EMPTY
						: user.getAddress().getDistrictName());
		userData.addProperty("mobile", StringUtils.isBlank(user.getMobile()) ? PLACEHOLDER_FOR_EMPTY : user.getMobile());

		return userData.toString();
	}

	private static Document loadIdCard() {
		Document doc = null;
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		String uri = System.getProperty("iCardPath", "classpath:/card/abhaCardNew.svg");
		try {
			doc = f.createDocument(uri);
		} catch (IOException e) {
			log.error("IOException occured:", e);
		}
		return doc;
	}

	/**
	 * SVG card With profile data (if True) or With URL Data (if False)
	 * 
	 * @param svgDoc
	 * @param user
	 * @throws WriterException
	 * @throws IOException
	 */
	private static void UpdateSVGCard(Document svgDoc, UserDTO user) throws WriterException, IOException {
		// SVG Card With Profile Data
		UpdateSVGCard(svgDoc, user, true);
	}

	private static void UpdateSVGCard(Document svgDoc, UserDTO user, Boolean isQrCodeWithProfileData)
			throws WriterException, IOException {
		try {

			// Name of the beneficiary
			Element elementName = svgDoc.getElementById("BENE_NAME_VALUE");
			elementName.setTextContent(
					StringUtils.isNoneBlank(user.getFullName()) ? user.getFullName() : user.getFirstName());

			// PHR ADDRESS of the beneficiary
			Element elementAddress = svgDoc.getElementById("ABHA_ADDRESS");
			elementAddress.setTextContent("ABHA Address - " + user.getPhrAddress());

			// PHR ADDRESS of the beneficiary
			Element elementAbhaNumber = svgDoc.getElementById("ABHA_NUMBER_VALUE");
			elementAbhaNumber.setTextContent(
					StringUtils.isNoneBlank(user.getHealthIdNumber()) ? user.getHealthIdNumber() : "NA");

			// YOB/DOB of the beneficiary
			Element elementDobTitle = svgDoc.getElementById("YEAR_OF_BIRTH_TEXT");
			if (StringUtils.isNoneBlank(user.getMonthOfBirth()) && StringUtils.isNoneBlank(user.getDayOfBirth())) {
				elementDobTitle.setTextContent("Date of Birth:");
			}
			Element elementDobValue = svgDoc.getElementById("YEAR_OF_BIRTH_VALUE");
			elementDobValue.setTextContent(GeneralUtils.getDobYobAsString(user.getDayOfBirth(), user.getMonthOfBirth(),
					user.getYearOfBirth()));

			// Gender of the beneficiary
			Element elementGender = svgDoc.getElementById("GENDER_VALUE");
			elementGender.setTextContent(Gender.byCode(user.getGender()).toString());

			// Profile PIC of the beneficiary
			SVGImageElement imgElementPic = (SVGImageElement) svgDoc.getElementById("PROFILE_PHOTO");
			if (StringUtils.isNoneBlank(user.getProfilePhoto())) {
				if (org.apache.commons.codec.binary.Base64.isBase64(user.getProfilePhoto())) {
					imgElementPic.getHref().setBaseVal("data:image/jpeg;base64," + user.getProfilePhoto());
				}
			}

			// QR CODE of the beneficiary
			String saltedValue = Argon2Encoder.encode(user.getPhrAddress(), PHRIdUtils.getSaltedValue(user));
			String QrData = isQrCodeWithProfileData ? generateQRCodeString(user)
					: generateQRCodeString(
							String.format(CardConstants.qrRedirectLink, user.getPhrAddress(), saltedValue));
			SVGImageElement imgElementQRImage = (SVGImageElement) svgDoc.getElementById("QR_CODE");
			if (org.apache.commons.codec.binary.Base64.isBase64(QrData)) {
				imgElementQRImage.getHref().setBaseVal("data:image/jpeg;base64," + QrData);
			}

		} catch (Exception e) {
			log.error("Error while generating svg card", e);
		}
	}
}
