/**
 * 
 */
package com.parserlabs.phr.utils;

import static com.parserlabs.phr.constants.Constants.CAPTCHA_ANSWER_KEY;
import static com.parserlabs.phr.constants.Constants.CAPTCHA_TEXT_KEY;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@UtilityClass
@Slf4j
public class GenerateCaptchaText {
	private final SecureRandom RAND = new SecureRandom();

	private final String[] OPERATIONS = { " + ", " - ", " x " };
	private String QUESTION_MARK = " ? ";
	private String EQUAL_MARK = " = ";

	public Map<String, String> generateCaptchaMathTextModel() {
		return generateCaptchaMathTextModel(null);
	}

	public Map<String, String> generateCaptchaMathTextModel(Integer caseId) {

		Map<String, String> captcha = new HashMap<>();
		int first;
		int second;
		int third;
		int op;
		int pos;
		int solution;

		op = RAND.nextInt(OPERATIONS.length);
		captcha.put("operation", OPERATIONS[op]);
		log.info("op: {}", op);
		switch (op) {
		case 2: // multiplication; limit generated args to [1-5]
			first = RAND.nextInt(5) + 1;
			second = RAND.nextInt(5) + 1;
			third = first * second;
			break;
		case 1: // subtraction
			first = RAND.nextInt(30);
			second = RAND.nextInt(30);
			if (first < second) {
				// reorder to ensure positive solution
				int tmp = first;
				first = second;
				second = tmp;
			}
			third = first - second;
			break;
		default: // addition
			first = RAND.nextInt(50);
			second = RAND.nextInt(50);
			third = first + second;
		}
		StringBuilder captchaText = new StringBuilder();
		pos = !Objects.isNull(caseId) ? caseId : RAND.nextInt(3);
		switch (pos) {
		case 0:
			solution = first;
			captchaText.append(QUESTION_MARK).append(OPERATIONS[op]).append(second).append(EQUAL_MARK).append(third);
			break;
		case 1:
			solution = second;
			captchaText.append(first).append(OPERATIONS[op]).append(QUESTION_MARK).append(EQUAL_MARK).append(third);
			break;
		default:
			solution = third;
			captchaText.append(first).append(OPERATIONS[op]).append(second).append(EQUAL_MARK).append(QUESTION_MARK);
			break;
		}

		captcha.put(CAPTCHA_TEXT_KEY, captchaText.toString());
		captcha.put(CAPTCHA_ANSWER_KEY, Integer.toString(solution));
		log.info("captcha:  {}", captcha.get(CAPTCHA_TEXT_KEY));
		return captcha;
	}

	public Map<String, String> generateCaptchaTextModel(int size) {
		Map<String, String> captcha = new HashMap<>();
		// to generate random integers in the range [0-61]

		// Characters to be included
		String chrs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

		// Generate n characters from above set and
		// add these characters to captcha.
		StringBuilder capText = new StringBuilder();
		while (size-- > 0) {
			int index = RAND.nextInt(62);
			capText.append(chrs.charAt(index));
		}
		captcha.put(CAPTCHA_TEXT_KEY, capText.toString());
		captcha.put(CAPTCHA_ANSWER_KEY, capText.toString());
		log.info("captcha:  {}", captcha.get(CAPTCHA_TEXT_KEY));
		return captcha;
	}

}
