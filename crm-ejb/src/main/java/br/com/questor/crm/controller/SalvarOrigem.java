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

import br.com.questor.crm.model.Origem;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarOrigem {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Origem> origemEventSrc;
	
	private Origem newOrigem;
	
	@Produces
	@Named
	public Origem getNewOrigem() {
		return newOrigem;
	}
	public String novo()
	{
		initNewOrigem();
		return "/pages/protected/user/origem?faces-redirect=true";
	}
	public String editar(Origem origem)
	{
		newOrigem = origem;
		return "/pages/protected/user/origem?faces-redirect=true";
	}
	public void salvar() throws Exception {
		log.info("Salvando Origem " + newOrigem.getDescricao());
		if(newOrigem.getId() == null)
		{
			em.persist(newOrigem);
		}
		else
		{
			em.merge(newOrigem);
		}
		origemEventSrc.fire(newOrigem);
		initNewOrigem();
	}
	public void excluir(Origem origem)
	{
		log.info("Excluindo Origem " + origem.getDescricao());
		em.remove(em.contains(origem) ? origem:em.merge(origem));
		origemEventSrc.fire(origem);
		initNewOrigem();
	}
	@PostConstruct
	public void initNewOrigem() {
		newOrigem = new Origem();
	}
}
