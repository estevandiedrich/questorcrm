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

import br.com.questor.crm.model.Proposta;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarProposta {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Proposta> propostaEventSrc;
	
	private Proposta newProposta;
	
	@Produces
	@Named
	public Proposta getNewProposta() {
		return newProposta;
	}
	public void setNewProposta(Proposta proposta)
	{
		newProposta = proposta;
	}
	
//	public void adicionarCotacao(Conta conta)
//	{
//		conta.getCotacoes().add(newProposta);
//		newProposta.setLead(conta);
//		em.persist(newProposta);
//	}
//	public void adicionar(String id)
//	{
//		Produto produto = em.find(Produto.class, Long.parseLong(id));
////		ProdutoCotacao produtoCotacao = new ProdutoCotacao();
////		produtoCotacao.setProduto(produto);
////		produtoCotacao.setCotacao(newProposta);
//		if(newProposta.getValorInicial() == null)
//		{
//			newProposta.setValorInicial(BigDecimal.ZERO);
//		}
//		newProposta.getProdutos().add(produto);
//	}
	
	public void salvar() throws Exception {
		log.info("Salvando Proposta" + newProposta.getDescricao());
		em.persist(newProposta);
		propostaEventSrc.fire(newProposta);
		initNewProposta();
	}

	@PostConstruct
	public void initNewProposta() {
		newProposta = new Proposta();
	}
}