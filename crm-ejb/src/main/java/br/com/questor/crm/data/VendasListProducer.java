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

import br.com.questor.crm.model.Venda;

@RequestScoped
public class VendasListProducer {
	@Inject
	private EntityManager em;
	
	private List<Venda> vendas;
	
	@Produces
	@Named
	public List<Venda> getVendas()
	{
		return vendas;
	}
	
	public void onVendaListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Venda venda) {
		retrieveAllVendasOrderedByData();
	}
	@PostConstruct
	public void retrieveAllVendasOrderedByData() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Venda> criteria = cb.createQuery(Venda.class);
		Root<Venda> venda = criteria.from(Venda.class);
		criteria.select(venda).orderBy(cb.asc(venda.get("dataVenda")));
		vendas = em.createQuery(criteria).getResultList();
	}
}
