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

import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.Oportunidade;

@RequestScoped
public class OportunidadeListProducer {
	@Inject
	private EntityManager em;
	
	private List<Oportunidade> oportunidades;
	
	@Produces
	@Named
	public List<Oportunidade> getOportunidades()
	{
		return oportunidades;
	}
	
	public void onOportunidadeListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Oportunidade oportunidade) {
		retrieveAllOportunidadesOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllOportunidadesOrderedByNome() {
//		@SuppressWarnings(value = "unchecked")
		oportunidades = em.createNamedQuery("Oportunidade.findAll").getResultList();
	}
}
