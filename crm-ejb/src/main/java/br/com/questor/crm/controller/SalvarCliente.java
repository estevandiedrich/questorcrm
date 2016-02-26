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

import br.com.questor.crm.model.Cliente;

@Stateful
@Model
public class SalvarCliente {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Cliente> clienteEventSrc;
	
	private Cliente newCliente;
	
	@Produces
	@Named
	public Cliente getNewCliente() {
		return newCliente;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando Cliente" + newCliente.getNome());
		em.persist(newCliente);
		clienteEventSrc.fire(newCliente);
		initNewCliente();
	}

	@PostConstruct
	public void initNewCliente() {
		newCliente = new Cliente();
	}
}
