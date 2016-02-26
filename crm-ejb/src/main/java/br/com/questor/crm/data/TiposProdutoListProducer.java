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

import br.com.questor.crm.model.TiposProduto;

@RequestScoped
public class TiposProdutoListProducer {
	@Inject
	private EntityManager em;
	
	private List<TiposProduto> tiposProduto;
	
	@Produces
	@Named
	public List<TiposProduto> getTiposProduto()
	{
		return tiposProduto;
	}
	
	public void onTiposProdutoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final TiposProduto tipoProduto) {
		retrieveAllTiposProdutosOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllTiposProdutosOrderedByDescricao() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TiposProduto> criteria = cb.createQuery(TiposProduto.class);
		Root<TiposProduto> tipoProduto = criteria.from(TiposProduto.class);
		criteria.select(tipoProduto).orderBy(cb.asc(tipoProduto.get("descricao")));
		tiposProduto = em.createQuery(criteria).getResultList();
	}
}
