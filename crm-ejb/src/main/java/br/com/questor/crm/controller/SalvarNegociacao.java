package br.com.questor.crm.controller;

import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.model.Proposta;
import br.com.questor.crm.model.Negociacao;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarNegociacao {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Negociacao> negociacaoEventSrc;
	
	@Inject
	private SalvarCotacao salvarCotacao;
	
	private Negociacao newNegociacao;
	
	private Proposta newCotacao;
	
	public void setNewCotacao(Proposta cotacao)
	{
		newCotacao = cotacao;
	}
	
	@Produces
	@Named
	public Negociacao getNewNegociacao() {
		return newNegociacao;
	}
	public void adicionar() throws Exception
	{
		log.info("Salvando Negociacao" + newNegociacao.getTexto());
		newNegociacao.setCotacao(newCotacao);
		newNegociacao.setDataEHora(new Date());
//		salvarCotacao.getNewCotacao().getNegociacoes().add(newNegociacao);
		em.persist(newNegociacao);
		initNewNegociacao();
	}
	public void salvar(Proposta cotacao) throws Exception {
		log.info("Salvando Negociacao" + newNegociacao.getTexto());
		newNegociacao.setCotacao(cotacao);
		em.persist(newNegociacao);
		negociacaoEventSrc.fire(newNegociacao);
		initNewNegociacao();
	}

	@PostConstruct
	public void initNewNegociacao() {
		newNegociacao = new Negociacao();
	}
}
