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
import br.com.questor.crm.model.Proposta;

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
	@PostConstruct
	public void retrieveAllProdutoModulosSelecionadossOrderedByDescricao() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProdutoModulosSelecionados> criteria = cb.createQuery(ProdutoModulosSelecionados.class);
		Root<ProdutoModulosSelecionados> produtoModuloSelecionado = criteria.from(ProdutoModulosSelecionados.class);
		criteria.select(produtoModuloSelecionado);
		produtoModulosSelecionados = em.createQuery(criteria).getResultList();
	}
	
	public List<ProdutoModulosSelecionados> retrieveAllProdutoModulosSelecionadosByPropostaOrderedByDescricao(Proposta proposta)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProdutoModulosSelecionados> criteria = cb.createQuery(ProdutoModulosSelecionados.class);
		Root<ProdutoModulosSelecionados> produtoModuloSelecionado = criteria.from(ProdutoModulosSelecionados.class);
		criteria.select(produtoModuloSelecionado).where(cb.equal(produtoModuloSelecionado.get("proposta"), proposta));
		return em.createQuery(criteria).getResultList();
	}
}