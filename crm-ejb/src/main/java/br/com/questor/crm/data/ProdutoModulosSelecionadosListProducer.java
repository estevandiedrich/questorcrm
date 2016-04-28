package br.com.questor.crm.data;

import java.util.List;

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
import br.com.questor.crm.model.ProdutoModulosSelecionados;

@RequestScoped
public class ProdutoModulosSelecionadosListProducer {
	@Inject
	private EntityManager em;
	
	private List<ProdutoModulosSelecionados> produtoModulosSelecionados;
	
	@Produces
	@Named
	public List<ProdutoModulosSelecionados> getProdutoModulosSelecionados()
	{
		return produtoModulosSelecionados;
	}
	
	public void onProdutoModulosSelecionadosListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final ProdutoModulosSelecionados produtoModuloSelecionado) {
		retrieveAllProdutoModulosSelecionadossOrderedByDescricao();
	}
//	@PostConstruct
	public void retrieveAllProdutoModulosSelecionadossOrderedByDescricao() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProdutoModulosSelecionados> criteria = cb.createQuery(ProdutoModulosSelecionados.class);
		Root<ProdutoModulosSelecionados> produtoModuloSelecionado = criteria.from(ProdutoModulosSelecionados.class);
		criteria.select(produtoModuloSelecionado);
		produtoModulosSelecionados = em.createQuery(criteria).getResultList();
	}
	
	public List<ProdutoModulosSelecionados> retrieveAllProdutoModulosSelecionadosByPropostaOrderedByDescricao(Cotacao cotacao)
	{
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<ProdutoModulosSelecionados> criteria = cb.createQuery(ProdutoModulosSelecionados.class);
//		Root<ProdutoModulosSelecionados> produtoModuloSelecionado = criteria.from(ProdutoModulosSelecionados.class);
//		criteria.select(produtoModuloSelecionado).where(cb.equal(produtoModuloSelecionado.get("cotacao"), cotacao));
//		return em.createQuery(criteria).getResultList();
		List<ProdutoModulosSelecionados> produtoModulosSelecionados = em.createNamedQuery("ProdutoModulosSelecionados.findByCotacao").setParameter("cotacao", cotacao.getId()).getResultList();
		return produtoModulosSelecionados;
	}
}
