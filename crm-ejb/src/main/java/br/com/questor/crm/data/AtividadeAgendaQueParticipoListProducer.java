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

import br.com.questor.crm.controller.LoginBean;
import br.com.questor.crm.model.AtividadeAgenda;

@RequestScoped
public class AtividadeAgendaQueParticipoListProducer {
	@Inject
	private EntityManager em;
	@Inject
	private LoginBean loginBean;
	
	private List<AtividadeAgenda> atividadeAgendaQueParticipo;
	
	@Produces
	@Named
	public List<AtividadeAgenda> getAtividadeAgendaQueParticipo()
	{
		return atividadeAgendaQueParticipo;
	}
	
	public void onAtividadeAgendaListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final AtividadeAgenda atividadeAgenda) {
		retrieveAllAtividadesAgendaOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllAtividadesAgendaOrderedByNome() {
		atividadeAgendaQueParticipo = em.createNamedQuery("AtividadeAgenda.findByPrincipal").setParameter("principal", loginBean.getPrincipalsFromDB().getId()).getResultList();
	}
}
