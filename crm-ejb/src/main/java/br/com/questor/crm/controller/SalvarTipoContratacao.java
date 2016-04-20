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

import br.com.questor.crm.model.TipoContratacao;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarTipoContratacao {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<TipoContratacao> tipoContratacaoEventSrc;
	
	private TipoContratacao newTipoContratacao;
	
	@Produces
	@Named
	public TipoContratacao getNewTipoContratacao() {
		return newTipoContratacao;
	}
	public String novo()
	{
		initNewTipoContratacao();
		return "/pages/protected/admin/tipocontratacao?faces-redirect=true";
	}
	public String editar(TipoContratacao tipoContratacao)
	{
		newTipoContratacao = tipoContratacao;
		return "/pages/protected/admin/tipocontratacao?faces-redirect=true";
	}
	public void excluir(TipoContratacao tipoContratacao)
	{
		log.info("Excluindo Cargo " + tipoContratacao.getDescricao());
		em.remove(em.contains(tipoContratacao) ? tipoContratacao:em.merge(tipoContratacao));
		tipoContratacaoEventSrc.fire(tipoContratacao);
		initNewTipoContratacao();
	}
	public void salvar() throws Exception {
		log.info("Salvando " + newTipoContratacao.getDescricao());
		if(newTipoContratacao.getId() == null)
		{
			em.persist(newTipoContratacao);
		}
		else
		{
			em.merge(newTipoContratacao);
		}
		tipoContratacaoEventSrc.fire(newTipoContratacao);
		initNewTipoContratacao();
	}

	@PostConstruct
	public void initNewTipoContratacao() {
		newTipoContratacao = new TipoContratacao();
	}
}
