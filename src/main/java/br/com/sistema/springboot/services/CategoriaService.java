package br.com.sistema.springboot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.sistema.springboot.domain.Categoria;
import br.com.sistema.springboot.respositories.CategoriaRepository;
import br.com.sistema.springboot.services.exceptions.IntegrityViolationException;
import br.com.sistema.springboot.services.exceptions.ObjectNotFoundException;


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
	
	public void delete(Integer id) {
		find(id);  //verifica se o id existe antes de deletar
		try{
			repo.delete(id);
		}catch(DataIntegrityViolationException e){
			throw new IntegrityViolationException("Não é possivel excluir uma categoria que possui produtos");
		}
	}

	public List<Categoria> findAll() {		
		return repo.findAll();
	}
}
