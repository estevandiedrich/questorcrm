package br.com.questor.crm.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.questor.crm.model.Produto;

@Path("/produtos")
@RequestScoped
public class ProdutosResourceRESTService {
	@Inject
	private EntityManager em;
	
	@GET
	@Produces("text/xml")
	public List<Produto> listaProdutos() {
	   @SuppressWarnings("unchecked")
	   final List<Produto> results = em.createQuery("select p from Produto p order by p.descricao").getResultList();
	   return results;
	}
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("text/xml")
	public Produto lookupProdutoById(@PathParam("id") long id) {
		return em.find(Produto.class, id);
	}
}
