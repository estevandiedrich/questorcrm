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

import br.com.questor.crm.model.Modulo;

@RequestScoped
public class ModulosDisponiveisListProducer {
	@Inject
	private EntityManager em;
	
	private List<Modulo> modulosDisponiveis;
	
	@Produces
	@Named
	public List<Modulo> getModulosDisponiveis()
	{
		return modulosDisponiveis;
	}
	
	public void onModuloListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Modulo modulo) {
		retrieveAllModulosOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllModulosOrderedByNome() {
		modulosDisponiveis = em.createNamedQuery("Modulo.findDisponiveis").getResultList();
	}
}
