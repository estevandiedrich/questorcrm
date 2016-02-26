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

import br.com.questor.crm.model.TiposProduto;

@Stateful
@Model
public class SalvarTipoProduto {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<TiposProduto> tiposProdutoEventSrc;
	
	private TiposProduto newTiposProduto;
	
	@Produces
	@Named
	public TiposProduto getNewTiposProduto() {
		return newTiposProduto;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando " + newTiposProduto.getDescricao());
		em.persist(newTiposProduto);
		tiposProdutoEventSrc.fire(newTiposProduto);
		initNewTiposProduto();
	}

	@PostConstruct
	public void initNewTiposProduto() {
		newTiposProduto = new TiposProduto();
	}
}
