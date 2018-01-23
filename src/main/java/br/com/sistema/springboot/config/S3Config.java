package br.com.sistema.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
@PropertySource(value = "file:\\${USERPROFILE}\\.spring_boot_ionic_S3.properties", ignoreResourceNotFound = true) // ${USERPROFILE} é a pasta home do Usuário, 
public class S3Config {
	
	@Autowired
	private Environment env;
	
	private String awsId ;
	
	private String awsKey ;
	
	@Value("${s3.region}")
	private String region ;
	
	@Bean
    public AmazonS3 s3Client(){
		
		awsId = env.getProperty("aws.access_key_id");
		awsKey = env.getProperty("aws.secret_access_key");	

		
		BasicAWSCredentials awsCred = new BasicAWSCredentials(awsId, awsKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
				            .withCredentials(new AWSStaticCredentialsProvider(awsCred)).build();
		
		return s3Client;
	}
}

