package br.com.questor.crm.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.model.Contato;
import br.com.questor.crm.model.Lead;

@Stateful
@Model
public class SalvarContato {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Contato> contatoEventSrc;
	
	private Contato newContato;
	
	@Produces
	@Named
	public Contato getNewContato() {
		return newContato;
	}
	public void adicionar(Lead lead)
	{
		log.info("Adicionando Contato" + newContato.getNome());
		if(newContato.getCargo() != null && newContato.getCargo().getId() == null)
		{
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não pode estar vazio.", null);
            FacesContext.getCurrentInstance().addMessage("regLeads:cargo", m);
		}
		else
		{
			lead.getContatos().add(newContato);
			initNewContato();
		}
	}
	public void salvar() throws Exception {
		log.info("Salvando Contato" + newContato.getNome());
		em.persist(newContato);
		contatoEventSrc.fire(newContato);
		initNewContato();
	}

	@PostConstruct
	public void initNewContato() {
		newContato = new Contato();
	}
}
