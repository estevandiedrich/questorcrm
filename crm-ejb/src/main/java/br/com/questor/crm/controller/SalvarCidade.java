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

import br.com.questor.crm.model.Cidade;

@Stateful
@Model
public class SalvarCidade {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Cidade> cidadeEventSrc;
	
	private Cidade newCidade;
	
	@Produces
	@Named
	public Cidade getNewCidade() {
		return newCidade;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando Cidade" + newCidade.getNome());
		em.persist(newCidade);
		cidadeEventSrc.fire(newCidade);
		initNewCidade();
	}

	@PostConstruct
	public void initNewCidade() {
		newCidade = new Cidade();
	}
}
