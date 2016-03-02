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

import br.com.questor.crm.controller.LoginBean;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.Principals;

@RequestScoped
public class LeadListProducer {
	@Inject
	private EntityManager em;
	@Inject
	private LoginBean loginBean;
	
	private List<Lead> leads;
	
	@Produces
	@Named
	public List<Lead> getLeads()
	{
		return leads;
	}
	
	public void onLeadListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Lead lead) {
		retrieveAllLeadsOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllLeadsOrderedByNome() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Lead> criteria = cb.createQuery(Lead.class);
		Root<Lead> leadRoot = criteria.from(Lead.class);
		Principals l = this.loginBean.getPrincipalsFromDB();
		if(loginBean.isCallerInRole("ADMIN"))
		{
			criteria.select(leadRoot).orderBy(cb.asc(leadRoot.get("nome")));
		}
		else
		{
			criteria.select(leadRoot).where(leadRoot.get("grupoUsuarios").in(l.getGruposUsuarios())).orderBy(cb.asc(leadRoot.get("nome")));
		}
		leads = em.createQuery(criteria).getResultList();
	}
}
