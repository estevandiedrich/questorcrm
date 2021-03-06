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

import br.com.questor.crm.model.ProdutoModulosSelecionados;
import br.com.questor.crm.model.TipoContratacao;

@RequestScoped
public class TiposContratacaoListProducer {
	@Inject
	private EntityManager em;
	
	private List<TipoContratacao> tiposContratacao;
	
	@Produces
	@Named
	public List<TipoContratacao> getTipoContratacao()
	{
		return tiposContratacao;
	}
	
	public void onTipoContratacaoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final TipoContratacao tipoProduto) {
		retrieveAllTipoContratacaosOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllTipoContratacaosOrderedByDescricao() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TipoContratacao> criteria = cb.createQuery(TipoContratacao.class);
		Root<TipoContratacao> tipoContratacao = criteria.from(TipoContratacao.class);
		criteria.select(tipoContratacao).orderBy(cb.asc(tipoContratacao.get("descricao")));
		tiposContratacao = em.createQuery(criteria).getResultList();
	}
	public TipoContratacao retrieveTipoContratacaoByProdutoModulosSelecionados(ProdutoModulosSelecionados produtoModulosSelecionados)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TipoContratacao> criteria = cb.createQuery(TipoContratacao.class);
		Root<TipoContratacao> tipoContratacao = criteria.from(TipoContratacao.class);
		criteria.select(tipoContratacao).where(cb.equal(tipoContratacao.get("produtoModulosSelecionados"), produtoModulosSelecionados));
		return em.createQuery(criteria).getSingleResult();
	}
}
