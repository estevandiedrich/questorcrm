package br.com.questor.crm.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.model.Produto;
@Stateful
@Model
public class SalvarProduto {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Produto> produtoEventSrc;
	
	private Produto newProduto;
	
	@Produces
	@Named
	public Produto getNewProduto() {
		return newProduto;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando " + newProduto.getDescricao());
		em.persist(newProduto);
		produtoEventSrc.fire(newProduto);
		initNewProduto();
	}

	@PostConstruct
	public void initNewProduto() {
		newProduto = new Produto();
	}
}
