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

import br.com.questor.crm.model.Atividade;

@RequestScoped
public class AtividadeListProducer {
	@Inject
	private EntityManager em;
	
	private List<Atividade> atividades;
	
	@Produces
	@Named
	public List<Atividade> getAtividades()
	{
		return atividades;
	}
	
	public void onAtividadeListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Atividade origem) {
		retrieveAllAtividadesOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllAtividadesOrderedByDescricao() {
		atividades = em.createNamedQuery("Atividade.findAll").getResultList();
	}
}
