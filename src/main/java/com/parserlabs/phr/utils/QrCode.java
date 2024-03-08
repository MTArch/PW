/**
 * 
 */
package com.parserlabs.phr.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.parserlabs.phr.enums.Colors;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@UtilityClass
@Slf4j
public class QrCode {

	private final String LOGO = "logo/nha_abha_logo.png";
	private final static int WIDTH = 350;
	private final static int HEIGHT = 350;

	public byte[] generateQRCodeImageWithLogo(String contentText) {
		// Create new configuration that specifies margin.
				Map<EncodeHintType, Integer> hints = new HashMap<>();
				hints.put(EncodeHintType.MARGIN, 0);


		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix bitMatrix = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			// Create a qr code with the url as content and a size of WxH px
			bitMatrix = writer.encode(contentText, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);

			// Load QR image
			BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());

			// Load logo image
			BufferedImage overly = getOverly(LOGO);
			
			// creates output image
			//	BufferedImage overly11 = new BufferedImage(WIDTH_LOGO, HEIGHT_LOGO, logoImage.getType());

			// Calculate the delta height and width between QR code and logo
			int deltaHeight = qrImage.getHeight() - overly.getHeight();
			int deltaWidth = qrImage.getWidth() - overly.getWidth();

			// Initialize combined image
			BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) combined.getGraphics();

			// Write QR code to new image at position 0/0
			g.drawImage(qrImage, 0, 0, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

			// Write logo into combine image at position (deltaWidth / 2) and
			// (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
			// the same space for the logo to be centered
			g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);

			// Write combined image as PNG to OutputStream
			ImageIO.write(combined, "png", os);

			return os.toByteArray();

		} catch (WriterException e) {
			log.error("WriterException occured", e);
		} catch (IOException e) {
			log.error("IOException occured", e);
		}
		return null;
	}

	private BufferedImage getOverly(String LOGO) throws IOException {
		return ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(LOGO));
	}

	private MatrixToImageConfig getMatrixConfig() {
		// ARGB Colors
		// Check Colors ENUM
		return new MatrixToImageConfig(Colors.BLACK.getArgb(), Colors.WHITE.getArgb());
	}

	/**
	 * Generate QR Code Image for give text.
	 * 
	 * @param text
	 * @return QR Code image in byte[]
	 * @throws WriterException
	 * @throws IOException
	 */
	public byte[] generateQRCodeImage(String text) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
		BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(qrCodeImage, "jpg", baos);

		return baos.toByteArray();
	}
	
}
