package br.com.questor.crm.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.questor.crm.controller.SalvarCotacao;
import br.com.questor.crm.model.Cotacao;
import br.com.questor.crm.model.Negociacao;

@RequestScoped
public class NegociacaoListProducer {
	@Inject
	private EntityManager em;
	
	private List<Negociacao> negociacoes;
	
	@Inject
	private SalvarCotacao salvarCotacao;
	
	@Produces
	@Named
	public List<Negociacao> getNegociacoes()
	{
		return negociacoes;
	}
	
	public void onNegociacaoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Negociacao negociacao) {
		retrieveAllNegociacoesOrderedByDataEHora();
	}
	
	public void retrieveAllNegociacoesByCotacaoOrderedByDataEHora(Cotacao cotacao)
	{
		salvarCotacao.setNewCotacao(cotacao);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Negociacao> criteria = cb.createQuery(Negociacao.class);
		Root<Negociacao> negociacao = criteria.from(Negociacao.class);
		criteria.select(negociacao).where(cb.equal(negociacao.get("cotacao"), cotacao)).orderBy(cb.desc(negociacao.get("dataEHora")));
		negociacoes = em.createQuery(criteria).getResultList();
//		return negociacoes;
	}
	
	@PostConstruct
	public void retrieveAllNegociacoesOrderedByDataEHora() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Negociacao> criteria = cb.createQuery(Negociacao.class);
		Root<Negociacao> negociacao = criteria.from(Negociacao.class);
		criteria.select(negociacao).orderBy(cb.desc(negociacao.get("dataEHora")));
		negociacoes = em.createQuery(criteria).getResultList();
	}
}
