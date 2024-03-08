package com.parserlabs.phr.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageUtils {

	public static byte[] compress(String actualImage) {
		byte[] compressedImage = null;

		if (StringUtils.hasText(actualImage)) {
			byte[] data = actualImage.getBytes();
			Deflater deflater = new Deflater();
			deflater.setInput(data);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
			deflater.finish();
			byte[] buffer = new byte[1024];
			while (!deflater.finished()) {
				int count = deflater.deflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			try {
				outputStream.close();
			} catch (Exception exp) {
				log.error("Exception occured while compressing the image. ", exp);
			}
			compressedImage = outputStream.toByteArray();
		}
		return compressedImage;
	}

	public static String decompress(byte[] compressedImage) {
		String actualImageString = null;

		if (Objects.nonNull(compressedImage) && compressedImage.length > 0) {
			Inflater inflater = new Inflater();
			inflater.setInput(compressedImage);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedImage.length);
			byte[] buffer = new byte[1024];
			while (!inflater.finished()) {
				int count = 0;
				try {
					count = inflater.inflate(buffer);
					if (count <= 0)
						break;
				} catch (DataFormatException e) {
					break;
				}
				outputStream.write(buffer, 0, count);
			}
			try {
				outputStream.close();
			} catch (IOException exp) {
				log.error("Exception occured while decompressing the image. {}", exp.getMessage());
			}
			byte[] actualImage = outputStream.toByteArray();

			actualImageString = Objects.nonNull(actualImage) && actualImage.length > 0 ? new String(actualImage) : null;
		}
		return actualImageString;
	}

}