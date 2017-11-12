package br.com.sistema.springboot.services.exceptions;

public class IntegrityViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IntegrityViolationException(String msg) {
		super(msg);

	}

	public IntegrityViolationException(String msg,Throwable cause) {
		super(msg,cause);

	}
	
}
