package br.com.questor.crm.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.model.Produto;
import br.com.questor.crm.model.ProdutoVenda;
import br.com.questor.crm.model.Venda;

@Stateful
//@Model
@Named("salvarVenda")
@SessionScoped
public class SalvarVenda {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Venda> vendaEventSrc;
	
	private Venda newVenda = new Venda();
	
	@Produces
	@Named
	public Venda getNewVenda() {
		return newVenda;
	}
	public void adicionar() throws Exception{
		this.newVenda.getProdutoVenda().setVenda(newVenda);
		this.newVenda.getProdutoVenda().setProduto(em.find(Produto.class, this.newVenda.getProdutoVenda().getProduto().getId()));
		this.newVenda.getProdutos().add(this.newVenda.getProdutoVenda());
		this.newVenda.setProdutoVenda(new ProdutoVenda());
	}
	public void salvar() throws Exception {
		log.info("Salvando Venda");
		em.persist(newVenda);
		vendaEventSrc.fire(newVenda);
		initNewVenda();
	}

	@PostConstruct
	public void initNewVenda() {
		newVenda = new Venda();
	}
}
