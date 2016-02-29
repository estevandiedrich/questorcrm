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

import br.com.questor.crm.model.Servico;
import br.com.questor.crm.model.TipoServico;

@Stateful
@Model
public class SalvarServico {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Servico> servicoEventSrc;
	
	private Servico newServico;
	
	@Produces
	@Named
	public Servico getNewServico() {
		return newServico;
	}
	
	public void salvar(String id) throws Exception {
		log.info("Salvando Serviço" + newServico.getDescricao());
		TipoServico newTipoServico = new TipoServico();
		newTipoServico.setId(Long.parseLong(id));
		newServico.setTipoServico(newTipoServico);
		em.persist(newServico);
		servicoEventSrc.fire(newServico);
		initNewServico();
	}

	@PostConstruct
	public void initNewServico() {
		newServico = new Servico();
	}
}
