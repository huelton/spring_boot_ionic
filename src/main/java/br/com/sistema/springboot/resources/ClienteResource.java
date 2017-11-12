package br.com.sistema.springboot.resources;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sistema.springboot.domain.Cliente;
import br.com.sistema.springboot.dto.ClienteDTO;
import br.com.sistema.springboot.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	
	@Autowired
	private ClienteService ClienteService;

	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id){
		Cliente obj = ClienteService.find(id);
		
		return ResponseEntity.ok().body(obj);
		
	}
	
	//INSERE UMA CATEGORIA
/*		@RequestMapping(method=RequestMethod.POST)
		public ResponseEntity<Void> insert(@Valid @RequestBody ClienteDTO objDto){
			Cliente obj = ClienteService.fromDTO(objDto);
			obj = ClienteService.insert(obj);
			URI uri =ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
			
			return ResponseEntity.created(uri).build();
		}*/
		
		//ATUALIZA UMA CATEGORIA
		@RequestMapping(value="/{id}", method=RequestMethod.PUT)
		public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id){
			Cliente obj = ClienteService.fromDTO(objDto);
			obj.setId(id);
			obj = ClienteService.update(obj);
			
			return ResponseEntity.noContent().build();
		}
		
		//DELETA UMA CATEGORIA
		@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
		public ResponseEntity<Void> delete(@PathVariable Integer id){
			ClienteService.delete(id);
			
			return ResponseEntity.noContent().build();		
		}
		
		//RETORNA TODAS AS CATEGORIAS
		@RequestMapping(method=RequestMethod.GET)
		public ResponseEntity<List<ClienteDTO>> findAll(){
			List <Cliente> list = ClienteService.findAll();
			List <ClienteDTO> listDto = list.stream().map(obj -> new ClienteDTO(obj)
					                                         ).collect(Collectors.toList()); //CONVERTE UMA LISTA PARA OUTRA LISTA
			 return ResponseEntity.ok().body(listDto);
			
		}
		
		//PAGINAÇÃO DE CATEGORIAS
		@RequestMapping(value="/page", method=RequestMethod.GET)
		public ResponseEntity<Page<ClienteDTO>> findPage(
				@RequestParam(value="page",defaultValue="0") Integer page, 
				@RequestParam(value="linesPerPage",defaultValue="24") Integer linesPerPage, 
				@RequestParam(value="orderBy",defaultValue="nome") String orderBy, 
				@RequestParam(value="direction",defaultValue="ASC") String direction){
			Page <Cliente> list = ClienteService.findPage(page, linesPerPage, orderBy, direction);
			Page <ClienteDTO> listDto = list.map(obj -> new ClienteDTO(obj)); //CONVERTE UMA PAGE PARA OUTRA PAGE DTO
			 return ResponseEntity.ok().body(listDto);
			
		}
		
	
}
