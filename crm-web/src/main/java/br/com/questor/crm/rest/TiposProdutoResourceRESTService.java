package br.com.questor.crm.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.questor.crm.model.TiposProduto;

@Path("/tiposproduto")
@RequestScoped
public class TiposProdutoResourceRESTService {
	@Inject
	private EntityManager em;
	
	@GET
	@Produces("text/xml")
	public List<TiposProduto> listaProdutos() {
		@SuppressWarnings("unchecked")
		final List<TiposProduto> results = em.createQuery("select tp from TiposProduto tp order by tp.descricao").getResultList();
		return results;
	}
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("text/xml")
	public TiposProduto lookupTiposProdutoById(@PathParam("id") long id) {
		return em.find(TiposProduto.class, id);
	}
}
