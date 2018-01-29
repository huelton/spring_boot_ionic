package br.com.sistema.springboot.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import br.com.sistema.springboot.domain.Cliente;
import br.com.sistema.springboot.dto.ClienteDTO;
import br.com.sistema.springboot.resources.exception.FieldMessage;
import br.com.sistema.springboot.respositories.ClienteRepository;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
    
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteUpdate ann){
		
	}
	
    @Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context){
		
    	@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    	Integer uriId = Integer.parseInt(map.get("id"));
    	
    	List<FieldMessage> list = new ArrayList<>();
        
    	Cliente aux = clienteRepository.findByEmail(objDto.getEmail());
    	if(aux != null && !aux.getId().equals(uriId)){
    		list.add(new FieldMessage("email","Email já existente"));
    	}

		for(FieldMessage e : list){
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		
		return list.isEmpty();
	}
}
