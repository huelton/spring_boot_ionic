package br.com.sistema.springboot.services;

import org.springframework.mail.SimpleMailMessage;

import br.com.sistema.springboot.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
