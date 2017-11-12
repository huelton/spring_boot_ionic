package br.com.sistema.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sistema.springboot.domain.Pedido;
import br.com.sistema.springboot.exceptions.ObjectNotFoundException;
import br.com.sistema.springboot.respositories.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	public Pedido find(Integer id){
		Pedido obj = repo.findOne(id);
		if(obj == null ){
			throw new ObjectNotFoundException("Objeto nao encontrado! id: "+id
					            +", tipo: "+Pedido.class.getName());
		}
		return obj;
	}
}
