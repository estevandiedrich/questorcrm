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

import br.com.questor.crm.model.LeadGroup;

@Stateful
@Model
public class SalvarLeadGroup {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<LeadGroup> leadGroupEventSrc;
	
	private LeadGroup newLeadGroup;
	
	@Produces
	@Named
	public LeadGroup getNewLeadGroup() {
		return newLeadGroup;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando LeadGroup" + newLeadGroup.getDescricao());
		em.persist(newLeadGroup);
		leadGroupEventSrc.fire(newLeadGroup);
		initNewLeadGroup();
	}

	@PostConstruct
	public void initNewLeadGroup() {
		newLeadGroup = new LeadGroup();
	}
}
