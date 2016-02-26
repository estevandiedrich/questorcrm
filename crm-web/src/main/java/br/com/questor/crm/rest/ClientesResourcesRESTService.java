package br.com.questor.crm.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.questor.crm.model.Cliente;

@Path("/clientes")
@RequestScoped
public class ClientesResourcesRESTService {
	@Inject
	private EntityManager em;
	
	@GET
	@Produces("text/xml")
	public List<Cliente> listaClientes() {
	   @SuppressWarnings("unchecked")
	   final List<Cliente> results = em.createQuery("select c from Cliente c order by c.nome").getResultList();
	   return results;
	}
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("text/xml")
	public Cliente lookupClienteById(@PathParam("id") long id) {
		return em.find(Cliente.class, id);
	}
}
