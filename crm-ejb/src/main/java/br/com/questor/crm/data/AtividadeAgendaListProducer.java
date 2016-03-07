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

import br.com.questor.crm.model.AtividadeAgenda;
import br.com.questor.crm.model.Lead;

@RequestScoped
public class AtividadeAgendaListProducer {
	@Inject
	private EntityManager em;
	
	private List<AtividadeAgenda> atividadesAgenda;
	
	@Produces
	@Named
	public List<AtividadeAgenda> getAtividadeAgendas()
	{
		return atividadesAgenda;
	}
	
	public void onAtividadeAgendaListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final AtividadeAgenda atividadeAgenda) {
		retrieveAllAtividadeAgendasOrderedByDataEHora();
	}
	@PostConstruct
	public void retrieveAllAtividadeAgendasOrderedByDataEHora() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AtividadeAgenda> criteria = cb.createQuery(AtividadeAgenda.class);
		Root<AtividadeAgenda> atividadeAgenda = criteria.from(AtividadeAgenda.class);
		criteria.select(atividadeAgenda).orderBy(cb.asc(atividadeAgenda.get("dataEHora")));
		atividadesAgenda = em.createQuery(criteria).getResultList();
	}
	public List<AtividadeAgenda> retrieveAllAtividadeAgendasByLeadOrderedByDataEHora(Lead lead) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AtividadeAgenda> criteria = cb.createQuery(AtividadeAgenda.class);
		Root<AtividadeAgenda> atividadeAgenda = criteria.from(AtividadeAgenda.class);
		criteria.select(atividadeAgenda).where(cb.equal(atividadeAgenda.get("lead"), lead)).orderBy(cb.asc(atividadeAgenda.get("dataEHora")));
		return em.createQuery(criteria).getResultList();
	}
}
