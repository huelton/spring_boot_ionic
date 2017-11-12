package br.com.sistema.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sistema.springboot.domain.Categoria;
import br.com.sistema.springboot.exceptions.ObjectNotFoundException;
import br.com.sistema.springboot.respositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id){
		Categoria obj = repo.findOne(id);
		if(obj == null ){
			throw new ObjectNotFoundException("Objeto nao encontrado! id: "+id
					            +", tipo: "+Categoria.class.getName());
		}
		return obj;
	}
	
	public Categoria insert(Categoria obj){
		obj.setId(null);
		return repo.save(obj);
	}

	public Categoria update(Categoria obj) {
		find(obj.getId());  //verifica se o id existe antes de atualizar
		return repo.save(obj);
	}
	
}
