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

import br.com.questor.crm.model.TipoContratacao;

@Stateful
@Model
public class SalvarTipoContratacao {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<TipoContratacao> tipoContratacaoEventSrc;
	
	private TipoContratacao newTipoContratacao;
	
	@Produces
	@Named
	public TipoContratacao getNewTipoContratacao() {
		return newTipoContratacao;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando " + newTipoContratacao.getDescricao());
		em.persist(newTipoContratacao);
		tipoContratacaoEventSrc.fire(newTipoContratacao);
		initNewTipoContratacao();
	}

	@PostConstruct
	public void initNewTipoContratacao() {
		newTipoContratacao = new TipoContratacao();
	}
}
