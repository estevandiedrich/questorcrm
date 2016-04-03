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

import br.com.questor.crm.model.Modulo;

@Stateful
@Model
public class SalvarModulo {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Modulo> moduloEventSrc;
	
	private Modulo newModulo;
	
	@Produces
	@Named
	public Modulo getNewModulo() {
		return newModulo;
	}
	public void excluir(Modulo modulo)
	{
		log.info("Excluindo Modulo " + modulo.getDescricao());
		em.remove(em.contains(modulo) ? modulo:em.merge(modulo));
		moduloEventSrc.fire(modulo);
		initNewModulo();
	}
	public void salvar() throws Exception {
		log.info("Salvando Modulo" + newModulo.getDescricao());
		em.persist(newModulo);
		moduloEventSrc.fire(newModulo);
		initNewModulo();
	}

	@PostConstruct
	public void initNewModulo() {
		newModulo = new Modulo();
	}
}
