package com.parserlabs.phr.addednew.objectstorageConfig;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsS3Configuration {

    @Value("${accessKey}")
    String accessKey;
    @Value("${secretKey}")
    String secretKey;
    @Value("${region}")
    String region;
    @Value("${aws.endpoint}")
    String endpoint;
    @Value("${aws.socket.timeout}")
    String sockTimeout;
    @Value("${aws.conn.timeout}")
    String connTimeout;

    public AWSCredentials credentials;
    public EndpointConfiguration configuration;
    public ClientConfiguration clientConfiguration;


    @Bean
    AmazonS3 createClient() {
        credentials = new BasicAWSCredentials(accessKey, secretKey);
        configuration = new EndpointConfiguration(endpoint, region);
        clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSocketTimeout(Integer.parseInt(sockTimeout));
        clientConfiguration.setConnectionTimeout(Integer.parseInt(connTimeout));


        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withEndpointConfiguration(configuration).withPathStyleAccessEnabled(true).withClientConfiguration(clientConfiguration).build();
        return s3client;
    }


}