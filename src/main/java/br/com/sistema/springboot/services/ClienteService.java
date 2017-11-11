package br.com.sistema.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sistema.springboot.domain.Cliente;
import br.com.sistema.springboot.exceptions.ObjectNotFoundException;
import br.com.sistema.springboot.respositories.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente buscar(Integer id){
		Cliente obj = repo.findOne(id);
		if(obj == null ){
			throw new ObjectNotFoundException("Objeto nao encontrado! id: "+id
					            +", tipo: "+Cliente.class.getName());
		}
		return obj;
	}
}
