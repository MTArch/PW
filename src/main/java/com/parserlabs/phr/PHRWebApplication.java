package com.parserlabs.phr;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@ComponentScan(basePackages = {"com.parserlabs.phr"})
@EnableFeignClients
@EnableJpaAuditing
@Import(BeanValidatorPluginsConfiguration.class)
@EnableScheduling
@EnableOpenApi
public class PHRWebApplication {

	public static void main(String[] args) {
		System.setProperty("isThreadContextMapInheritable", "true");
		ThreadContext.put("host", System.getenv("POD_NAME"));
		SpringApplication.run(PHRWebApplication.class, args);
	}

}