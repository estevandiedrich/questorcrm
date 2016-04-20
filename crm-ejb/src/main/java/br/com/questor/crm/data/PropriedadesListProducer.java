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

import br.com.questor.crm.model.Propriedades;

@RequestScoped
public class PropriedadesListProducer {
	@Inject
	private EntityManager em;
	
	private List<Propriedades> propriedades;
	
	@Produces
	@Named
	public List<Propriedades> getPropriedades()
	{
		return propriedades;
	}
	
	public void onPropriedadesListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Propriedades cargo) {
		retrieveAllPropriedadesOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllPropriedadesOrderedByNome() {
		propriedades = em.createNamedQuery("Propriedades.findAll").getResultList();
	}
}
