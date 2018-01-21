package br.com.sistema.springboot.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.sistema.springboot.domain.Cidade;
import br.com.sistema.springboot.domain.Cliente;
import br.com.sistema.springboot.domain.Endereco;
import br.com.sistema.springboot.domain.enums.Perfil;
import br.com.sistema.springboot.domain.enums.TipoCliente;
import br.com.sistema.springboot.dto.ClienteDTO;
import br.com.sistema.springboot.dto.ClienteNewDTO;
import br.com.sistema.springboot.respositories.CidadeRepository;
import br.com.sistema.springboot.respositories.ClienteRepository;
import br.com.sistema.springboot.respositories.EnderecoRepository;
import br.com.sistema.springboot.security.UserSS;
import br.com.sistema.springboot.services.exceptions.AuthorizationException;
import br.com.sistema.springboot.services.exceptions.IntegrityViolationException;
import br.com.sistema.springboot.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCrypt;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	public Cliente find(Integer id){
		
		UserSS user = UserService.authenticated();
		if(user==null || !user.hasHole(Perfil.ADMIN) && !id.equals(user.getId())){
			throw new AuthorizationException("Acesso Negado!");
		}
		
		
		Cliente obj = clienteRepository.findOne(id);
		if(obj == null ){
			throw new ObjectNotFoundException("Objeto nao encontrado! id: "+id
					            +", tipo: "+Cliente.class.getName());
		}
		return obj;
	}
	
	public Cliente insert(Cliente obj){
		obj.setId(null);
		obj = clienteRepository.save(obj);
		enderecoRepository.save(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());  //verifica se o id existe antes de atualizar
		updateData(newObj, obj);
		return clienteRepository.save(newObj);
	}
	

	public void delete(Integer id) {
		find(id);  //verifica se o id existe antes de deletar
		try{
			clienteRepository.delete(id);
		}catch(DataIntegrityViolationException e){
			throw new IntegrityViolationException("Não é possivel excluir porque há pedidos relacionados");
		}
	}

	public List<Cliente> findAll() {		
		return clienteRepository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDt){
		return new Cliente(objDt.getId(), objDt.getNome(), objDt.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto){
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), 
				                  TipoCliente.toEnum(objDto.getTipo()), bCrypt.encode(objDto.getSenha()));
		Cidade cid = cidadeRepository.findOne(objDto.getCidadeId());
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), 
				objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		
		if(objDto.getTelefone2()!=null){
			cli.getTelefones().add(objDto.getTelefone2());
		}
		
		if(objDto.getTelefone3()!=null){
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
		
	}	
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());		
	}

	public URI uploadProfilePicture(MultipartFile multipartFile){
		
		UserSS user = UserService.authenticated();
		if(user == null){
			throw new AuthorizationException("Acesso Negado!");
		}
	
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");

	}
}
