package br.com.sistema.springboot.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.sistema.springboot.domain.Cliente;
import br.com.sistema.springboot.domain.enums.TipoCliente;
import br.com.sistema.springboot.dto.ClienteNewDTO;
import br.com.sistema.springboot.resources.exception.FieldMessage;
import br.com.sistema.springboot.respositories.ClienteRepository;
import br.com.sistema.springboot.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
    
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteInsert ann){
		
	}
	
    @Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context){
		
    	List<FieldMessage> list = new ArrayList<>();
        
    	if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())){
    		list.add(new FieldMessage("cpfOuCnpj","CPF Inválido"));
    	}
    	
    	if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())){
    		list.add(new FieldMessage("cpfOuCnpj","CNPJ Inválido"));
    	}

    	Cliente emailBanco = clienteRepository.findByEmail(objDto.getEmail());
    	if(emailBanco != null){
    		list.add(new FieldMessage("email","Email já existente"));
    	}

		for(FieldMessage e : list){
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		
		return list.isEmpty();
	}
}
