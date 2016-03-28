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
public class ProdutosComModuloListProducer {
	@Inject
	private EntityManager em;
	
	private List<Produto> produtosComModulo;
	
	@Produces
	@Named
	public List<Produto> getProdutosComModulo()
	{
		return produtosComModulo;
	}
	
	public void onProdutoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Produto produto) {
		retrieveAllProdutosOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllProdutosOrderedByDescricao() {
		produtosComModulo = em.createNamedQuery("Produto.findAllComModulo").getResultList();
	}
}
