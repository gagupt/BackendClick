package com.example.demo.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Component
public class S3Bean {

  private AWSCredentialsProvider getS3Credentials(String accessKey, String secret) {
    AWSCredentialsProvider provider = new EC2ContainerCredentialsProviderWrapper();
    if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secret)) {
      provider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secret));
    }
    return new AWSCredentialsProviderChain(provider);
  }

  @Bean
  public AmazonS3 buildClient() {
    AWSCredentialsProvider provider =
        getS3Credentials("AKIAIQYNP5Z24XLJ6NAA", "3/Zga+YGJ3CtCcmy/Sxwo/+8H0XePbI2GcJck4LA");
    return AmazonS3ClientBuilder.standard().withPathStyleAccessEnabled(true)
        .withRegion("ap-south-1").withCredentials(provider).build();
  }
}
