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
	public void editarModulo(Modulo modulo)
	{
		newProduto.setModulo(modulo);
	}
	public void excluirModulo(Modulo modulo)
	{
		log.info("Excluindo Modulo " + modulo.getDescricao());
		em.remove(em.contains(modulo) ? modulo:em.merge(modulo));
		newProduto.getModulos().remove(modulo);
	}
	public void adicionar(Modulo modulo)
	{
		if(modulo.getId() != null)
		{
			em.merge(modulo);
		}
		else
		{
			newProduto.getModulos().add(modulo);
		}
		newProduto.setModulo(new Modulo());
	}
	public void excluir(Produto produto)
	{
		log.info("Excluindo Produto " + produto.getDescricao());
		em.remove(em.contains(produto) ? produto:em.merge(produto));
		produtoEventSrc.fire(produto);
		initNewProduto();
	}
	public void salvar() throws Exception {
		log.info("Salvando " + newProduto.getDescricao());
		if(newProduto.getId() == null)
		{
			em.persist(newProduto);
			for(Modulo modulo:newProduto.getModulos())
			{
				modulo.setProduto(newProduto);				
				em.persist(modulo);
			}
		}
		else
		{
			em.merge(newProduto);
			for(Modulo modulo:newProduto.getModulos())
			{
				modulo.setProduto(newProduto);
				if(modulo.getId() == null)
				{
					em.persist(modulo);
				}
			}
		}
		produtoEventSrc.fire(newProduto);
		initNewProduto();
	}

	@PostConstruct
	public void initNewProduto() {
		newProduto = new Produto();
	}
}
