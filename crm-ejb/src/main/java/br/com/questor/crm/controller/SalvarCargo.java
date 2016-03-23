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

import br.com.questor.crm.model.Cargo;

@Stateful
@Model
public class SalvarCargo {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Cargo> cargoEventSrc;
	
	private Cargo newCargo;
	
	@Produces
	@Named
	public Cargo getNewCargo() {
		return newCargo;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando Cargo" + newCargo.getDescricao());
		em.persist(newCargo);
		cargoEventSrc.fire(newCargo);
		initNewCargo();
	}

	@PostConstruct
	public void initNewCargo() {
		newCargo = new Cargo();
	}
}
