package br.com.questor.crm.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.questor.crm.model.Venda;

@Path("/vendas")
@RequestScoped
public class VendasResourcesRESTServices {
	@Inject
	private EntityManager em;
	
	@GET
	@Produces("text/xml")
	public List<Venda> listaProdutos() {
	   @SuppressWarnings("unchecked")
	   final List<Venda> results = em.createQuery("select v from Venda v order by v.dataVenda").getResultList();
	   return results;
	}
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("text/xml")
	public Venda lookupVendaById(@PathParam("id") long id) {
		return em.find(Venda.class, id);
	}
}
