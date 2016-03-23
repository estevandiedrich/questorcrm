package br.com.questor.crm.controller;

import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.data.ModuloListProducer;
import br.com.questor.crm.data.ModuloSelecionadoListProducer;
import br.com.questor.crm.model.Modulo;
import br.com.questor.crm.model.ModuloSelecionado;
import br.com.questor.crm.model.Produto;
import br.com.questor.crm.model.ProdutoModulosSelecionados;
import br.com.questor.crm.model.Cotacao;
import br.com.questor.crm.model.TipoContratacao;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarProdutoModulosSelecionados {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private ModuloListProducer moduloListProducer;
	
	@Inject
	private ModuloSelecionadoListProducer moduloSelecionadoListProducer;
	
	@Inject
	private Event<ProdutoModulosSelecionados> produtoModulosSelecionadosEventSrc;
	
	private ProdutoModulosSelecionados newProdutoModulosSelecionados;
	
	@Produces
	@Named
	public ProdutoModulosSelecionados getNewProdutoModulosSelecionados() {
		return newProdutoModulosSelecionados;
	}
	public void setProdutoModulosSelecionados(ProdutoModulosSelecionados produto)
	{
		newProdutoModulosSelecionados = produto;
		if(newProdutoModulosSelecionados.getId() != null)
		{
			List<ModuloSelecionado> modulosSelecionados = moduloSelecionadoListProducer.retrieveAllModuloSelecionadoByProdutoModulosSelecionados(newProdutoModulosSelecionados);
			List<Modulo> modulos = moduloListProducer.retrieveAllModulosByProdutoOrderedByNome(newProdutoModulosSelecionados.getProduto());
			newProdutoModulosSelecionados.getProduto().setModulos(modulos);
			newProdutoModulosSelecionados.setModulosSelecionados(modulosSelecionados);
		}
	}
	public void calculaValorTotal()
	{
		if(newProdutoModulosSelecionados.getQuantidade() != null && newProdutoModulosSelecionados.getValorUnitario() != null)
		{
			newProdutoModulosSelecionados.setValorTotal(newProdutoModulosSelecionados.getQuantidade().multiply(newProdutoModulosSelecionados.getValorUnitario()));
			newProdutoModulosSelecionados.getValorTotal().setScale(2, RoundingMode.HALF_UP);
		}
	}
	public void setProduto() {
		if(newProdutoModulosSelecionados.getProduto().getId() != null)
		{
			Long id = newProdutoModulosSelecionados.getProduto().getId();
			Produto p = em.find(Produto.class, id);
			List<Modulo> modulos = moduloListProducer.retrieveAllModulosByProdutoOrderedByNome(p);
			p.setModulos(modulos);
			newProdutoModulosSelecionados.setProduto(p);
		}
    }
	public void adicionarModulo(Long id)
	{
		Modulo modulo = em.find(Modulo.class, id);
		ModuloSelecionado moduloSelecionado = new ModuloSelecionado();
		moduloSelecionado.setModulo(modulo);
		newProdutoModulosSelecionados.getModulosSelecionados().add(moduloSelecionado);
	}
	public void adicionar(Cotacao cotacao) throws Exception
	{
		log.info("Salvando " + newProdutoModulosSelecionados.getId());
		
		TipoContratacao tipoContratacao = em.find(TipoContratacao.class, newProdutoModulosSelecionados.getTipoContratacao().getId());
		newProdutoModulosSelecionados.setTipoContratacao(tipoContratacao);
		for(ModuloSelecionado moduloSelecionado:newProdutoModulosSelecionados.getModulosSelecionados())
		{
			moduloSelecionado.setProdutosModulosSelecionados(newProdutoModulosSelecionados);
		}
		cotacao.getProdutosModulosSelecionados().add(newProdutoModulosSelecionados);
		initNewProdutoModulosSelecionados();
	}
	public void salvar() throws Exception {
		log.info("Salvando " + newProdutoModulosSelecionados.getId());
		
		TipoContratacao tipoContratacao = em.find(TipoContratacao.class, newProdutoModulosSelecionados.getTipoContratacao().getId());
		newProdutoModulosSelecionados.setTipoContratacao(tipoContratacao);
		
		em.persist(newProdutoModulosSelecionados);
		for(ModuloSelecionado moduloSelecionado:newProdutoModulosSelecionados.getModulosSelecionados())
		{
			moduloSelecionado.setProdutosModulosSelecionados(newProdutoModulosSelecionados);
			em.persist(moduloSelecionado);
		}
		produtoModulosSelecionadosEventSrc.fire(newProdutoModulosSelecionados);
		initNewProdutoModulosSelecionados();
	}

	@PostConstruct
	public void initNewProdutoModulosSelecionados() {
		newProdutoModulosSelecionados = new ProdutoModulosSelecionados();
	}
}