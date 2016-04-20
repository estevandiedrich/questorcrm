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

import br.com.questor.crm.model.Origem;

@RequestScoped
public class OrigemListProducer {
	@Inject
	private EntityManager em;
	
	private List<Origem> origens;
	
	@Produces
	@Named
	public List<Origem> getOrigens()
	{
		return origens;
	}
	
	public void onOrigemListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Origem origem) {
		retrieveAllOrigemsOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllOrigemsOrderedByDescricao() {
		origens = em.createNamedQuery("Origem.findAll").getResultList();
	}
}
