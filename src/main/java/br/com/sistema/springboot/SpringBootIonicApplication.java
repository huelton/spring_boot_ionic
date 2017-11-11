package br.com.sistema.springboot;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.sistema.springboot.domain.Categoria;
import br.com.sistema.springboot.domain.Cidade;
import br.com.sistema.springboot.domain.Cliente;
import br.com.sistema.springboot.domain.Endereco;
import br.com.sistema.springboot.domain.Estado;
import br.com.sistema.springboot.domain.Produto;
import br.com.sistema.springboot.domain.enums.TipoCliente;
import br.com.sistema.springboot.respositories.CategoriaRepository;
import br.com.sistema.springboot.respositories.CidadeRepository;
import br.com.sistema.springboot.respositories.ClienteRepository;
import br.com.sistema.springboot.respositories.EnderecoRepository;
import br.com.sistema.springboot.respositories.EstadoRepository;
import br.com.sistema.springboot.respositories.ProdutoRepository;

@SpringBootApplication
public class SpringBootIonicApplication implements CommandLineRunner {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootIonicApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		Categoria cat1 = new Categoria(null, "Informatica");
		Categoria cat2 = new Categoria(null, "Escritorio");
		
		Produto p1 = new Produto(null,"Computador",2000.00);
		Produto p2 = new Produto(null,"Impressora",800.00);
		Produto p3 = new Produto(null,"Mouse",50.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat1.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		categoriaRepository.save(Arrays.asList(cat1,cat2));		
		produtoRepository.save(Arrays.asList(p1,p2,p3));
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null,"Uberlandia",est1);
		Cidade c2 = new Cidade(null,"São Paulo",est2);
		Cidade c3 = new Cidade(null,"Campinas",est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		
		estadoRepository.save(Arrays.asList(est1,est2));		
		cidadeRepository.save(Arrays.asList(c1,c2,c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com","11111111111", TipoCliente.PESSOAFISICA);
	    cli1.getTelefones().addAll(Arrays.asList("23455633","55332333"));
	    
	    Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38200922", cli1, c1);
	    Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "40400010", cli1, c2);
	    
	    cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
	    
	    clienteRepository.save(Arrays.asList(cli1));
	    enderecoRepository.save(Arrays.asList(e1,e2));
	    
  	}
	
}