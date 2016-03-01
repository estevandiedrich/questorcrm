package br.com.questor.crm.controller;

import java.math.BigDecimal;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.model.Conta;
import br.com.questor.crm.model.Proposta;
import br.com.questor.crm.model.Produto;

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
	private Event<Proposta> cotacaoEventSrc;
	
	private Proposta newCotacao;
	
	@Produces
	@Named
	public Proposta getNewCotacao() {
		return newCotacao;
	}
	public void setNewCotacao(Proposta cotacao)
	{
		newCotacao = cotacao;
	}
	public void adicionarCotacao(Conta conta)
	{
		conta.getCotacoes().add(newCotacao);
		newCotacao.setLead(conta);
		em.persist(newCotacao);
	}
	public void adicionar(String id)
	{
		Produto produto = em.find(Produto.class, Long.parseLong(id));
//		ProdutoCotacao produtoCotacao = new ProdutoCotacao();
//		produtoCotacao.setProduto(produto);
//		produtoCotacao.setCotacao(newCotacao);
		if(newCotacao.getValorInicial() == null)
		{
			newCotacao.setValorInicial(BigDecimal.ZERO);
		}
		newCotacao.getProdutos().add(produto);
	}
	
	public void salvar() throws Exception {
		log.info("Salvando Cotacao" + newCotacao.getDescricao());
		em.persist(newCotacao);
		cotacaoEventSrc.fire(newCotacao);
		initNewCotacao();
	}

	@PostConstruct
	public void initNewCotacao() {
		newCotacao = new Proposta();
	}
}