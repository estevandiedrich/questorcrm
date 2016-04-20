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

import br.com.questor.crm.model.Atividade;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarAtividade {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Atividade> atividadeEventSrc;
	
	private Atividade newAtividade;
	
	@Produces
	@Named
	public Atividade getNewAtividade() {
		return newAtividade;
	}
	public String novo()
	{
		initNewAtividade();
		return "/pages/protected/user/atividade?faces-redirect=true";
	}
	public String editar(Atividade atividade)
	{
		newAtividade = atividade;
		return "/pages/protected/user/atividade?faces-redirect=true";
	}
	public void salvar() throws Exception {
		log.info("Salvando Atividade " + newAtividade.getDescricao());
		if(newAtividade.getId() == null)
		{
			em.persist(newAtividade);
		}
		else
		{
			em.merge(newAtividade);
		}
		atividadeEventSrc.fire(newAtividade);
		initNewAtividade();
	}
	public void excluir(Atividade atividade)
	{
		log.info("Excluindo Atividade " + atividade.getDescricao());
		em.remove(em.contains(atividade) ? atividade:em.merge(atividade));
		atividadeEventSrc.fire(atividade);
		initNewAtividade();
	}
	@PostConstruct
	public void initNewAtividade() {
		newAtividade = new Atividade();
	}
}
