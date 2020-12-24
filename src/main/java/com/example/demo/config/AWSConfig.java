package com.example.demo.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AWSConfig {
	@Value("${AWS_ACCESS_KEY_ID}")
	private String accessKey;

	@Value("${AWS_SECRET_ACCESS_KEY}")
	private String secretKey;

	@Value("${AWS_REGION}")
	private String region;

	@Bean
	public AmazonS3 s3() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(
				accessKey,
				secretKey
		);

		return AmazonS3ClientBuilder
				.standard()
				.withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.withPathStyleAccessEnabled(true)
				.build();
	}
}
