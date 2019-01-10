package com.example.demo.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.example.demo.services"})
public class DynamoDbConfig {

  @Value("${amazon.dynamodb.endpoint}")
  private String amazonDynamoDBEndpoint;

  @Value("${amazon.aws.accesskey}")
  private String amazonAWSAccessKey;

  @Value("${amazon.aws.secretkey}")
  private String amazonAWSSecretKey;

  @Bean
  public AmazonDynamoDB amazonDynamoDB() {
    AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());
    if (StringUtils.isNotEmpty(amazonDynamoDBEndpoint)) {
      amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
    }
    return amazonDynamoDB;
  }

  @Bean
  public AWSCredentials amazonAWSCredentials() {
    return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
  }

  @Bean
  public DynamoDB amazonDynamoDb() {
    AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(
        new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey));
    EndpointConfiguration endpointConfig =
        new EndpointConfiguration(amazonDynamoDBEndpoint, "ap-south-1");
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(provider)
        .withEndpointConfiguration(endpointConfig).build();
    return new DynamoDB(client);

  }

}
