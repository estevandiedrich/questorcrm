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

import br.com.questor.crm.model.TipoServico;

@Stateful
@Model
public class SalvarTipoServico {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<TipoServico> tipoServicoEventSrc;
	
	private TipoServico newTipoServico;
	
	@Produces
	@Named
	public TipoServico getNewTipoServico() {
		return newTipoServico;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando " + newTipoServico.getDescricao());
		em.persist(newTipoServico);
		tipoServicoEventSrc.fire(newTipoServico);
		initNewTiposServico();
	}

	@PostConstruct
	public void initNewTiposServico() {
		newTipoServico = new TipoServico();
	}
}
