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

import br.com.questor.crm.model.LeadGroup;

@RequestScoped
public class LeadGroupListProducer {
	@Inject
	private EntityManager em;
	
	private List<LeadGroup> leadGroups;
	
	@Produces
	@Named
	public List<LeadGroup> getLeadGroups()
	{
		return leadGroups;
	}
	
	public void onLeadGroupListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final LeadGroup leadGroup) {
		retrieveAllLeadGroupsOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllLeadGroupsOrderedByDescricao() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<LeadGroup> criteria = cb.createQuery(LeadGroup.class);
		Root<LeadGroup> leadGroupRoot = criteria.from(LeadGroup.class);
		criteria.select(leadGroupRoot).orderBy(cb.asc(leadGroupRoot.get("descricao")));
		leadGroups = em.createQuery(criteria).getResultList();
	}
}
