package br.com.sistema.springboot.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.sistema.springboot.domain.Categoria;
import br.com.sistema.springboot.dto.CategoriaDTO;
import br.com.sistema.springboot.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {	
	
	@Autowired
	private CategoriaService categoriaService;//CAMADA DE SERVIÇOS

	//ENCONTRA UMA CATEGORIA PELO ID
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id){
		Categoria obj = categoriaService.find(id);
		
		return ResponseEntity.ok().body(obj);		
	}
	
	//INSERE UMA CATEGORIA
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto){
		Categoria obj = categoriaService.fromDTO(objDto);
		obj = categoriaService.insert(obj);
		URI uri =ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	//ATUALIZA UMA CATEGORIA
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id){
		Categoria obj = categoriaService.fromDTO(objDto);
		obj.setId(id);
		obj = categoriaService.update(obj);
		
		return ResponseEntity.noContent().build();
	}
	
	//DELETA UMA CATEGORIA
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		categoriaService.delete(id);
		
		return ResponseEntity.noContent().build();		
	}
	
	//RETORNA TODAS AS CATEGORIAS
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll(){
		List <Categoria> list = categoriaService.findAll();
		List <CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)
				                                         ).collect(Collectors.toList()); //CONVERTE UMA LISTA PARA OUTRA LISTA
		 return ResponseEntity.ok().body(listDto);
		
	}
	
	//PAGINAÇÃO DE CATEGORIAS
	@RequestMapping(value="/page", method=RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page",defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage",defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy",defaultValue="nome") String orderBy, 
			@RequestParam(value="direction",defaultValue="ASC") String direction){
		Page <Categoria> list = categoriaService.findPage(page, linesPerPage, orderBy, direction);
		Page <CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj)); //CONVERTE UMA PAGE PARA OUTRA PAGE DTO
		 return ResponseEntity.ok().body(listDto);
		
	}
	
	
}
