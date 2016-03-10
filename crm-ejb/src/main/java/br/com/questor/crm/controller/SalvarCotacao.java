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

import br.com.questor.crm.model.Cotacao;
import br.com.questor.crm.model.Proposta;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarCotacao {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Cotacao> cotacaoEventSrc;
	
	private Cotacao newCotacao;
	
	@Produces
	@Named
	public Cotacao getNewCotacao() {
		return newCotacao;
	}
	public void adicionar() throws Exception
	{
		log.info("Salvando Cotação" + newCotacao.getTexto());
		newCotacao.setDataEHora(new Date());
		em.persist(newCotacao);
		initNewCotacao();
	}
	public void salvar(Proposta proposta) throws Exception {
		log.info("Salvando Negociacao" + newCotacao.getTexto());
		newCotacao.setProposta(proposta);
		em.persist(newCotacao);
		cotacaoEventSrc.fire(newCotacao);
		initNewCotacao();
	}

	@PostConstruct
	public void initNewCotacao() {
		newCotacao = new Cotacao();
	}
}
