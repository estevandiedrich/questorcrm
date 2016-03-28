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

import br.com.questor.crm.model.Produto;

@RequestScoped
public class ProdutosListProducer {
	@Inject
	private EntityManager em;
	
	private List<Produto> produtos;
	
	@Produces
	@Named
	public List<Produto> getProdutos()
	{
		return produtos;
	}
	
	public void onProdutoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Produto produto) {
		retrieveAllProdutosOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllProdutosOrderedByDescricao() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Produto> criteria = cb.createQuery(Produto.class);
//		Root<Produto> produto = criteria.from(Produto.class);
//		criteria.select(produto).orderBy(cb.asc(produto.get("descricao")));
//		produtos = em.createQuery(criteria).getResultList();
//		for(Produto p:produtos)
//		{
//			List<Modulo> m = moduloListProducer.retrieveAllModulosByProdutoOrderedByNome(p);
//			p.setModulos(m);
//		}
		produtos = em.createNamedQuery("Produto.findAll").getResultList();
	}
}
