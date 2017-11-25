package br.com.sistema.springboot.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.sistema.springboot.domain.Cliente;
import br.com.sistema.springboot.domain.ItemPedido;
import br.com.sistema.springboot.domain.PagamentoComBoleto;
import br.com.sistema.springboot.domain.Pedido;
import br.com.sistema.springboot.domain.enums.EstadoPagamento;
import br.com.sistema.springboot.domain.enums.Perfil;
import br.com.sistema.springboot.respositories.ClienteRepository;
import br.com.sistema.springboot.respositories.ItemPedidoRepository;
import br.com.sistema.springboot.respositories.PagamentoRepository;
import br.com.sistema.springboot.respositories.PedidoRepository;
import br.com.sistema.springboot.respositories.ProdutoRepository;
import br.com.sistema.springboot.security.UserSS;
import br.com.sistema.springboot.services.exceptions.AuthorizationException;
import br.com.sistema.springboot.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EmailService emailService;
	
	
	public Pedido find(Integer id){
		Pedido obj = pedidoRepository.findOne(id);
		if(obj == null ){
			throw new ObjectNotFoundException("Objeto nao encontrado! id: "+id
					            +", tipo: "+Pedido.class.getName());
		}
		
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasHole(Perfil.ADMIN) && !obj.getCliente().getId().equals(user.getId())){
			throw new AuthorizationException("Acesso Negado!");
		}
		
		return obj;
	}

	public Pedido insert(Pedido obj) {
		
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteRepository.findOne(obj.getCliente().getId()));
		obj.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto){
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for(ItemPedido itemPedido : obj.getItens()){
			itemPedido.setDesconto(0.0);
			itemPedido.setProduto(produtoRepository.findOne(itemPedido.getProduto().getId()));
			itemPedido.setPreco(itemPedido.getProduto().getPreco());
			itemPedido.setPedido(obj);
		}
		itemPedidoRepository.save(obj.getItens());		
		
		emailService.sendOrderConfirmationEmail(obj);	
		
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){			
		UserSS user = UserService.authenticated();
		if(user==null){
			throw new AuthorizationException("Acesso Negado!");
		}			
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteRepository.findOne(user.getId());
		return pedidoRepository.findByCliente(cliente, pageRequest);
	}
	
}
