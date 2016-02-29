package br.com.questor.crm.controller;

import java.math.BigDecimal;
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
import br.com.questor.crm.model.Servico;
import br.com.questor.crm.model.TiposProduto;
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
	
	private Produto newProduto;
	
	@Produces
	@Named
	public Produto getNewProduto() {
		return newProduto;
	}
	
	public void adicionar(String id)
	{
		Servico servico = em.find(Servico.class, Long.parseLong(id));		
		newProduto.getServicos().add(servico);
		if(newProduto.getValor() == null)
		{
			newProduto.setValor(BigDecimal.ZERO);
		}
		newProduto.setValor(newProduto.getValor().add(servico.getValor()));
	}
	
	public void salvar(String id) throws Exception {
		log.info("Salvando " + newProduto.getDescricao());
		TiposProduto newTiposProduto = em.find(TiposProduto.class, Long.parseLong(id));
		newProduto.setTipoProduto(newTiposProduto);
		em.persist(newProduto);
		for(Servico servico:newProduto.getServicos())
		{
			servico.setProduto(newProduto);
			em.merge(servico);
		}
		produtoEventSrc.fire(newProduto);
		initNewProduto();
	}

	@PostConstruct
	public void initNewProduto() {
		newProduto = new Produto();
	}
}
