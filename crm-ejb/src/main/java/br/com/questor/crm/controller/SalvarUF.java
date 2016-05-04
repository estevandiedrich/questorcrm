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

import br.com.questor.crm.model.UF;

@Stateful
//@Model
@Named
@SessionScoped
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
	public String novo()
	{
		initNewUf();
		return "/pages/protected/user/uf?faces-redirect=true";
	}
	public String editar(UF uf)
	{
		newUf = uf;
		return "/pages/protected/user/uf?faces-redirect=true";
	}
	public void excluir(UF uf)
	{
		log.info("Excluindo uf " + uf.getNome());
		em.remove(em.contains(uf) ? uf:em.merge(uf));
		ufEventSrc.fire(uf);
		initNewUf();
	}
	public void salvar() throws Exception {
		log.info("Salvando UF" + newUf.getNome());
		if(newUf.getId() == null)
		{
			em.persist(newUf);
		}
		else
		{
			em.merge(newUf);
		}
		ufEventSrc.fire(newUf);
		initNewUf();
	}

	@PostConstruct
	public void initNewUf() {
		newUf = new UF();
	}
}
