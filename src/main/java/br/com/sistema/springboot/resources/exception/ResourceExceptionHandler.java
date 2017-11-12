package br.com.sistema.springboot.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.sistema.springboot.services.exceptions.IntegrityViolationException;
import br.com.sistema.springboot.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e,HttpServletRequest request){
		
		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(),e.getMessage(),System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(IntegrityViolationException.class)
	public ResponseEntity<StandardError> DataIntegrity(IntegrityViolationException e,HttpServletRequest request){
		
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(),e.getMessage(),System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	 
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> Validation(MethodArgumentNotValidException e,HttpServletRequest request){
		
		ValidationError validErr = new ValidationError(HttpStatus.BAD_REQUEST.value(),"Erro de Validação",System.currentTimeMillis());
		for(FieldError x : e.getBindingResult().getFieldErrors()){
			validErr.addError(x.getField(), x.getDefaultMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validErr);
	}
	 
}
