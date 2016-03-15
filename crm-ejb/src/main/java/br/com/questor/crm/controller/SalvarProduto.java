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

import br.com.questor.crm.data.ModuloListProducer;
import br.com.questor.crm.model.Modulo;
import br.com.questor.crm.model.Produto;
@Stateful
//@Model
@Named
@SessionScoped
public class SalvarProduto {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Produto> produtoEventSrc;
	
	@Inject
	private ModuloListProducer moduloListProducer;
	
	private Produto newProduto;
	
	@Produces
	@Named
	public Produto getNewProduto() {
		return newProduto;
	}
	
	public void setNewProduto(Produto produto)
	{
		this.newProduto = produto;
		newProduto.setModulos(moduloListProducer.retrieveAllModulosByProdutoOrderedByNome(newProduto));
	}
	
	public String novo()
	{
		initNewProduto();
		return "/pages/protected/admin/produtos?faces-redirect=true";
	}
	
	public void adicionar(String id)
	{
		Modulo modulo = em.find(Modulo.class, Long.parseLong(id));
		newProduto.getModulos().add(modulo);
	}
	
	public void salvar() throws Exception {
		log.info("Salvando " + newProduto.getDescricao());
		em.persist(newProduto);
		for(Modulo modulo:newProduto.getModulos())
		{
			modulo.setProduto(newProduto);
			em.merge(modulo);
		}
		produtoEventSrc.fire(newProduto);
		initNewProduto();
	}

	@PostConstruct
	public void initNewProduto() {
		newProduto = new Produto();
	}
}
