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

import br.com.questor.crm.model.Cidade;

@RequestScoped
public class CidadeComUFListProducer {
	@Inject
	private EntityManager em;
	
	private List<Cidade> cidadesComUF;
	
	@Produces
	@Named
	public List<Cidade> getCidadesComUF()
	{
		return cidadesComUF;
	}
	
	public void onCidadeListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Cidade cidade) {
		retrieveAllCidadesOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllCidadesOrderedByNome() {
		cidadesComUF = em.createNamedQuery("Cidade.findAllComUF").getResultList();
	}
}
