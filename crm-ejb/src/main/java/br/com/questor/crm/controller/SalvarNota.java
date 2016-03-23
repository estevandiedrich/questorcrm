package br.com.questor.crm.controller;

import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.Nota;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarNota {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Nota> notaEventSrc;
	
	private Nota newNota;
	
	@Produces
	@Named
	public Nota getNewNota() {
		return newNota;
	}
	public void adicionar(Lead lead) throws Exception
	{
		log.info("Salvando nota" + newNota.getDescricao());
		newNota.setDataEHora(new Date());
		lead.getNotas().add(newNota);
		initNewNota();
	}
	public void salvar(Lead lead) throws Exception {
		log.info("Salvando lead" + newNota.getTexto());
		newNota.setLead(lead);
		em.persist(newNota);
		notaEventSrc.fire(newNota);
		initNewNota();
	}

	@PostConstruct
	public void initNewNota() {
		newNota = new Nota();
	}
}
