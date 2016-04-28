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

import br.com.questor.crm.model.Cidade;

@Stateful
//@Model
@Named
@SessionScoped
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
	public String novo()
	{
		initNewCidade();
		return "/pages/protected/user/cidade?faces-redirect=true";
	}
	public String editar(Cidade cidade)
	{
		newCidade = cidade;
		return "/pages/protected/user/cidade?faces-redirect=true";
	}
	public void excluir(Cidade cidade)
	{
		log.info("Excluindo cidade " + cidade.getNome());
		em.remove(em.contains(cidade) ? cidade:em.merge(cidade));
		cidadeEventSrc.fire(cidade);
		initNewCidade();
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
