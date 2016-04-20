package br.com.questor.crm.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.model.Propriedades;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarPropriedade {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Propriedades> propriedadeEventSrc;
	
	private Propriedades newPropriedade;
	
	@Produces
	@Named
	public Propriedades getNewPropriedade() {
		return newPropriedade;
	}
	public String novo()
	{
		initNewPropriedades();
		return "/pages/protected/admin/propriedades?faces-redirect=true";
	}
	public String editar(Propriedades propriedade)
	{
		newPropriedade = propriedade;
		return "/pages/protected/admin/propriedades?faces-redirect=true";
	}
	public void salvar() throws Exception {
		log.info("Salvando Propriedades " + newPropriedade.getChave());
		if(newPropriedade.getId() == null)
		{
			em.persist(newPropriedade);
		}
		else
		{
			em.merge(newPropriedade);
		}
		propriedadeEventSrc.fire(newPropriedade);
		initNewPropriedades();
	}
	public void excluir(Propriedades propriedade)
	{
		log.info("Excluindo Propriedades " + propriedade.getChave());
		em.remove(em.contains(propriedade) ? propriedade:em.merge(propriedade));
		propriedadeEventSrc.fire(propriedade);
		initNewPropriedades();
	}
	@PostConstruct
	public void initNewPropriedades() {
		newPropriedade = new Propriedades();
	}
}
