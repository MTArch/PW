package com.parserlabs.phr.security.captcha;

import java.awt.Color;
import java.awt.Font;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.BackgroundProducer;
import cn.apiclub.captcha.backgrounds.TransparentBackgroundProducer;
import cn.apiclub.captcha.noise.CurvedLineNoiseProducer;
import cn.apiclub.captcha.noise.NoiseProducer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.producer.TextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import cn.apiclub.captcha.text.renderer.WordRenderer;

/**
 * 
 * @author Rajesh kumar
 *
 */
@Component
public class CaptchaGenerator {

	private BackgroundProducer backgroundProducer = new TransparentBackgroundProducer();
	private TextProducer textProducer = new DefaultTextProducer();
	private WordRenderer wordRenderer = new DefaultWordRenderer(textColors(), textFonts());
	private NoiseProducer noiseProducer = new CurvedLineNoiseProducer(getNoiseColor(), 1.0f);

	public Captcha createCaptcha(int width, int height) {
		return new Captcha.Builder(width, height).addBackground(backgroundProducer).addText(textProducer, wordRenderer)
				.addNoise(noiseProducer).build();
	}

	public CaptchaGenerator() {
		if (this.backgroundProducer == null) {
			this.backgroundProducer = new TransparentBackgroundProducer();
		}
		if (this.textProducer == null) {
			this.textProducer = new DefaultTextProducer();
		}
		if (this.wordRenderer == null) {
			this.wordRenderer = new DefaultWordRenderer();
		}
		if (this.noiseProducer == null) {
			this.noiseProducer = new CurvedLineNoiseProducer();
		}
	}

	public CaptchaGenerator(TextProducer textProducer, BackgroundProducer backgroundProducer) {
		this.backgroundProducer = backgroundProducer;
		this.textProducer = textProducer;

	}

	public CaptchaGenerator(TextProducer textProducer) {
		this.textProducer = textProducer;
	}

	private List<Color> textColors() {
		return Arrays.asList(Color.BLACK, Color.BLUE, Color.RED, Color.DARK_GRAY, Color.MAGENTA, Color.GRAY,
				new Color(25, 25, 112), new Color(0, 0, 128), new Color(0, 0, 139), new Color(0, 0, 205),
				new Color(0, 0, 255), new Color(65, 105, 225), new Color(138, 43, 226), new Color(75, 0, 130),
				new Color(72, 61, 139), new Color(106, 90, 205), new Color(123, 104, 238), new Color(128, 0, 0),
				new Color(139, 0, 0), new Color(165, 42, 42), new Color(178, 34, 34), new Color(0, 100, 0),
				new Color(102, 205, 170), new Color(25, 25, 112), new Color(0, 0, 128), new Color(0, 0, 139),
				new Color(0, 0, 205), new Color(65, 105, 225), new Color(138, 43, 226), new Color(72, 61, 139),
				new Color(75, 0, 130), new Color(106, 90, 205), new Color(123, 104, 238), new Color(147, 112, 219),
				new Color(139, 0, 139), new Color(148, 0, 211), new Color(153, 50, 204), new Color(186, 85, 211),
				new Color(128, 0, 128));
	}

	private Color getNoiseColor() {
		SecureRandom rand = new SecureRandom();
		int size = noiseColors().size();
		return noiseColors().get(rand.nextInt(size - 1));
	}

	private List<Color> noiseColors() {
		return Arrays.asList(Color.CYAN, Color.YELLOW, new Color(154, 205, 50), new Color(127, 255, 0),
				new Color(255, 160, 122), new Color(255, 140, 0), new Color(238, 232, 170), new Color(255, 250, 205),
				new Color(218, 112, 214), new Color(255, 228, 225), new Color(255, 240, 245), new Color(250, 240, 230),
				new Color(253, 245, 230), new Color(255, 239, 213), new Color(255, 245, 238), new Color(245, 255, 250),
				new Color(112, 128, 144));
	}

	private List<Font> textFonts() {
		return Arrays.asList(new Font("Arial", Font.ITALIC, 40), new Font("Arial", Font.PLAIN, 40),
				new Font("Courier", Font.BOLD, 40), new Font("Courier", Font.ITALIC, 40),
				new Font(Font.SANS_SERIF, Font.PLAIN, 40), new Font(Font.SANS_SERIF, Font.PLAIN, 40),
				new Font("Helvetica", Font.BOLD, 40));
	}

}
