package com.parserlabs.phr.service;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
//import com.parserlabs.phr.AddedNew.S3Metric;
import com.parserlabs.phr.addednew.S3Metric;

//import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class S3StorageService {

    @Value("${bucket}")
    private String bucket;


    @Autowired
    private AmazonS3 s3client;


    @S3Metric
    public String fetchProfilePhoto(String identifier) {
        try {
            log.info("Fetching Profile Photo");
            System.out.println(identifier);
            S3Object object = s3client.getObject(bucket, identifier);
            S3ObjectInputStream imagestream = object.getObjectContent();
            byte[] photo;
            photo = imagestream.readAllBytes();
            imagestream.close();
            object.close();
            return new String(Base64.encodeBase64(photo));
        } catch (SocketTimeoutException e) {
            log.warn("Error fetching data from object storage , storage not available {}", e.getMessage());
            return null;
        } catch (IOException e) {
            log.error("Error while processing fetch data {}", e.getMessage());
            return null;
        }  catch (AmazonS3Exception e) {
            log.warn("Error fetching data from object storage key probably does not exist {}", e.getMessage());
            return null;
        } catch (SdkClientException e) {
            log.error("Error fetching data from object storage , storage not available {}", e.getMessage());
            return null;
        }
    }


    @S3Metric
    public String setProfilePhoto(String identifier, byte[] image, boolean isPhotoEncoded) {
        try {
            log.info("Putting Profile Photo {}", new String(image));


            try {
                if (isPhotoEncoded) {
                    image = Base64.decodeBase64(image);

                    InputStream inputStream = new ByteArrayInputStream(image);
                    ObjectMetadata metdata = new ObjectMetadata();
                    metdata.setContentLength(image.length);
                    s3client.putObject(bucket, identifier, inputStream, metdata);
                    inputStream.close();

                    log.info("Image uploaded tp Object Storage Successfully");
                    return identifier;

                } else {
                    InputStream inputStream = new ByteArrayInputStream(image);
                    ObjectMetadata metdata = new ObjectMetadata();
                    metdata.setContentLength(image.length);
                    s3client.putObject(bucket, identifier, inputStream, metdata);
                    inputStream.close();

                    log.info("Image uploaded tp Object Storage Successfully");
                    return identifier;
                }

            } catch (AmazonServiceException e) {
                log.error("Exception While Putting image in object storage {}", e.getMessage());
                return null;
            } catch (IOException e) {
                log.error("Error while closing input stream {}", e.getMessage());
                return null;
            } catch (SdkClientException e) {
                log.warn("Error Putting data into object storage {}", e.getMessage());
                return null;
            }
        } catch (Exception e) {
            log.error("Error while processing  for data {}", e.getMessage());
            return null;
        }

    }


    public Boolean deletePhoto(String identifier) {
        try {
            log.info("Deleting Profile Photo");
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, identifier);
            s3client.deleteObject(deleteObjectRequest);
            return true;
        } catch (AmazonServiceException e) {
            log.error("Exception While Putting image in object storage", e.getMessage());
            return false;
        } catch (SdkClientException e) {
            log.warn("Error fetching data from object storage , storage not available", e.getMessage());
            return false;
        }
    }


    public Boolean keyExists(long key) {
        return s3client.doesObjectExist(bucket, String.valueOf(key));
    }

}