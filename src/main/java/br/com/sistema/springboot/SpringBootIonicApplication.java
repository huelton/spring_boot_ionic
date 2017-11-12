package br.com.sistema.springboot;

import java.text.SimpleDateFormat;
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
import br.com.sistema.springboot.domain.ItemPedido;
import br.com.sistema.springboot.domain.Pagamento;
import br.com.sistema.springboot.domain.PagamentoComBoleto;
import br.com.sistema.springboot.domain.PagamentoComCartao;
import br.com.sistema.springboot.domain.Pedido;
import br.com.sistema.springboot.domain.Produto;
import br.com.sistema.springboot.domain.enums.EstadoPagamento;
import br.com.sistema.springboot.domain.enums.TipoCliente;
import br.com.sistema.springboot.respositories.CategoriaRepository;
import br.com.sistema.springboot.respositories.CidadeRepository;
import br.com.sistema.springboot.respositories.ClienteRepository;
import br.com.sistema.springboot.respositories.EnderecoRepository;
import br.com.sistema.springboot.respositories.EstadoRepository;
import br.com.sistema.springboot.respositories.ItemPedidoRepository;
import br.com.sistema.springboot.respositories.PagamentoRepository;
import br.com.sistema.springboot.respositories.PedidoRepository;
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
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootIonicApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informatica");
		Categoria cat2 = new Categoria(null, "Escritorio");
		Categoria cat3 = new Categoria(null, "Cama, Mesa e Banho");
		Categoria cat4 = new Categoria(null, "Eletrônicos");
		Categoria cat5 = new Categoria(null, "Jardinagem");
		Categoria cat6 = new Categoria(null, "Decoração");
		Categoria cat7 = new Categoria(null, "Perfumaria");
		
		categoriaRepository.save(Arrays.asList(cat1,cat2,cat3,cat4,cat5,cat6,cat7));	
		
		//
		Produto p1 = new Produto(null,"Computador",2000.00);
		Produto p2 = new Produto(null,"Impressora",800.00);
		Produto p3 = new Produto(null,"Mouse",50.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat1.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
					
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
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
	    
	    Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:36"), cli1,e1);
	    
	    Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 18:23"), cli1,e2);
	    
	    Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
	    ped1.setPagamento(pagto1);
	    
	    Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE,ped2, sdf.parse("20/10/2017 00:00"), null);
	    ped2.setPagamento(pagto2);
	    
	    cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));
	    
        pedidoRepository.save(Arrays.asList(ped1,ped2));
        pagamentoRepository.save(Arrays.asList(pagto1,pagto2));
        
        ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
        ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
        ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
        
        ped1.getItens().addAll(Arrays.asList(ip1,ip2));
        ped2.getItens().addAll(Arrays.asList(ip3));
        
        p1.getItens().addAll(Arrays.asList(ip1));
        p2.getItens().addAll(Arrays.asList(ip3));
        p3.getItens().addAll(Arrays.asList(ip2));
        
        itemPedidoRepository.save(Arrays.asList(ip1,ip2,ip3));
  	}
	
}