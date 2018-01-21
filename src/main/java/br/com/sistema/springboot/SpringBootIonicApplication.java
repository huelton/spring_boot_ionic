package br.com.sistema.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.sistema.springboot.services.S3Service;

@SpringBootApplication
public class SpringBootIonicApplication implements CommandLineRunner {
	
	@Autowired
	private S3Service s3Service;
		
	public static void main(String[] args) {
		SpringApplication.run(SpringBootIonicApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		s3Service.uploadFile("C:\\temp\\asm_imagem2.png");	
  	}
	
}