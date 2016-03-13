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

import br.com.questor.crm.model.UF;

@Stateful
@Model
public class SalvarUF {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<UF> ufEventSrc;
	
	private UF newUf;
	
	@Produces
	@Named
	public UF getNewUf() {
		return newUf;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando UF" + newUf.getNome());
		em.persist(newUf);
		ufEventSrc.fire(newUf);
		initNewUf();
	}

	@PostConstruct
	public void initNewUf() {
		newUf = new UF();
	}
}
