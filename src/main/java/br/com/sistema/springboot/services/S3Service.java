package br.com.sistema.springboot.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import br.com.sistema.springboot.services.exceptions.FileException;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	

	
	@Autowired
	private AmazonS3 s3Client;
	
	
	@Value("${s3.bucket}") // ${USERPROFILE} é a pasta home do Usuário, 
	private String bucketName;
	

	public URI uploadFile(MultipartFile multipartFile) {

		try {			
			
			String fileName = multipartFile.getOriginalFilename();
			InputStream inputStream = multipartFile.getInputStream();
			String contentType = multipartFile.getContentType();

			return uploadFile(inputStream, fileName, contentType);
		} catch (IOException e) {
			throw new FileException("Erro de IO: " + e.getMessage());
		}

	}

	public URI uploadFile(InputStream inputStream, String fileName, String contentType) {

		try {
			ObjectMetadata metadado = new ObjectMetadata();
			metadado.setContentType(contentType);
			LOG.info("Iniciando Upload");
			s3Client.putObject(bucketName, fileName, inputStream, metadado);
			LOG.info("Upload Finalizado");

			return s3Client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new FileException("Erro ao converter URL para URI");
		}
	}
}
