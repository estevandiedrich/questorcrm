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

import br.com.questor.crm.model.Cliente;

@RequestScoped
public class ClientesListProducer {
	@Inject
	private EntityManager em;
	
	private List<Cliente> clientes;
	
	@Produces
	@Named
	public List<Cliente> getClientes()
	{
		return clientes;
	}
	
	public void onClienteListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Cliente cliente) {
		retrieveAllClientesOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllClientesOrderedByNome() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cliente> criteria = cb.createQuery(Cliente.class);
		Root<Cliente> cliente = criteria.from(Cliente.class);
		criteria.select(cliente).orderBy(cb.asc(cliente.get("nome")));
		clientes = em.createQuery(criteria).getResultList();
	}
}
