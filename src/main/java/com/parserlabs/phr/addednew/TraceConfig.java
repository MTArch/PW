package com.parserlabs.phr.addednew;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import feign.Client;
import okhttp3.OkHttpClient;

@Configuration
public class TraceConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        return restTemplate;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
    @Bean
    public Client feignClient() {
      return new Client.Default(null, null);
    }
}
