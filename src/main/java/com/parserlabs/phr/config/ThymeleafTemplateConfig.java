/**
 * 
 */
package com.parserlabs.phr.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author Rajesh
 * 
 *         Configure a SpringTemplateEngine especially configured for
 *         email processing, in our Spring boot email template configuration. We
 *         need to tell Thymeleaf where the email templates are located. We do
 *         this by creating and configuring the SpringResourceTemplateResolver.
 *         We can set a prefix and suffix to configure where Thymeleaf will
 *         search for the HTML email templates
 *
 */
@Configuration
public class ThymeleafTemplateConfig {
	@Bean
	public SpringTemplateEngine springTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(htmlTemplateResolver());
		return templateEngine;
	}

	@Bean
	public SpringResourceTemplateResolver htmlTemplateResolver() {
		SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
		emailTemplateResolver.setPrefix("classpath:/templates/");
		emailTemplateResolver.setSuffix(".html");
		emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
		emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		return emailTemplateResolver;
	}

}
