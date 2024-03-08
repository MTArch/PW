package com.parserlabs.phr.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	private static final String BASE_PACKAGE = "com.parserlabs.phr.controller.v1";
	private static final boolean ENABLE_SWAGGER = true;
	private static final String API_VERSION_V1 = "V.1.0";

	private static final String GROUP_NAME_V1 = "PHR Sevice V1";
	private static final String PATH_NAME_V1 = "/api/v1/**";

	@Autowired
	MessageSource messages;

	@Value("${enable.swagger:true}")
	private boolean enableSwagger;
	// Define the Spec version v.1.0
	@Bean
	public Docket swaggerPhrApiV1() {

		return new Docket(DocumentationType.OAS_30)
				.groupName(GROUP_NAME_V1)
				.select()
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
				.paths(PathSelectors.ant(PATH_NAME_V1)).build()
				.pathMapping("/")
				.apiInfo(apiEndPointsInfoV1())
				.securitySchemes(apiKeys())
				.securityContexts(Arrays.asList(securityContext())).enable(ENABLE_SWAGGER)
				.enable(enableSwagger);
	}

	private ApiInfo apiEndPointsInfoV1() {
		return new ApiInfoBuilder().title(messages.getMessage("swagger.api.title", null, Locale.ENGLISH))
				.description(messages.getMessage("swagger.api.description", null, Locale.ENGLISH))
				.version(API_VERSION_V1).build();
	}

	private List<SecurityScheme> apiKeys() {
		List<SecurityScheme> keys = new ArrayList<SecurityScheme>();
		keys.add(new ApiKey("Authorization", "Authorization", "header"));
		keys.add(new ApiKey("X-HIP-ID", "X-HIP-ID", "header"));
		return keys;
	}

	@SuppressWarnings("deprecation")
	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Authorization", authorizationScopes),
				new SecurityReference("X-HIP-ID", authorizationScopes));
	}

}
