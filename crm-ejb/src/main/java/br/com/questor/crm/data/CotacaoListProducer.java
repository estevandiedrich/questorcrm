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

import br.com.questor.crm.model.Cotacao;
import br.com.questor.crm.model.Lead;

@RequestScoped
public class CotacaoListProducer {
	@Inject
	private EntityManager em;
	
	private List<Cotacao> cotacoes;
	
	@Produces
	@Named
	public List<Cotacao> getCotacoes()
	{
		return cotacoes;
	}
	
	public void onCotacaoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Cotacao cotacao) {
		retrieveAllCotacoesOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllCotacoesOrderedByDescricao() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cotacao> criteria = cb.createQuery(Cotacao.class);
		Root<Cotacao> cotacao = criteria.from(Cotacao.class);
		criteria.select(cotacao).orderBy(cb.asc(cotacao.get("descricao")));
		cotacoes = em.createQuery(criteria).getResultList();
	}
	public List<Cotacao> retrieveAllCotacoesByLeadOrderedByDataEHoraCriacao(Lead lead) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cotacao> criteria = cb.createQuery(Cotacao.class);
		Root<Cotacao> cotacao = criteria.from(Cotacao.class);
		criteria.select(cotacao).where(cb.equal(cotacao.get("lead"), lead)).orderBy(cb.asc(cotacao.get("dataEHoraCriacao")));
//		criteria.select(cotacao).orderBy(cb.asc(cotacao.get("descricao")));
		cotacoes = em.createQuery(criteria).getResultList();
		return cotacoes;
	}
}
