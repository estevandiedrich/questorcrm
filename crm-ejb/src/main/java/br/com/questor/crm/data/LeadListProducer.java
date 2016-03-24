package br.com.questor.crm.data;

import java.util.ArrayList;
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
import br.com.questor.crm.model.GrupoUsuariosLead;
import br.com.questor.crm.model.GrupoUsuariosPrincipals;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.Principals;

@RequestScoped
public class LeadListProducer {
	@Inject
	private EntityManager em;
	@Inject
	private LoginBean loginBean;
	@Inject
	private GrupoUsuariosPrincipalsListProducer grupoUsuariosPrincipalsListProducer;
	@Inject
	private GrupoUsuariosLeadListProducer grupoUsuariosLeadListProducer;
	
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
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Lead> criteria = cb.createQuery(Lead.class);
//		Root<Lead> leadRoot = criteria.from(Lead.class);
		Principals l = this.loginBean.getPrincipalsFromDB();
		if(loginBean.isCallerInRole("ADMIN"))
		{
//			criteria.select(leadRoot).orderBy(cb.asc(leadRoot.get("nome")));
			leads = em.createNamedQuery("Lead.findAll").getResultList();
		}
		else
		{
			List<GrupoUsuariosPrincipals> grupoUsuariosPrincipals = grupoUsuariosPrincipalsListProducer.retrieveAllGrupoUsuariosPrincipalsByPrincipal(l);
			l.setGruposUsuarios(grupoUsuariosPrincipals);
			List<GrupoUsuariosLead> grupoUsuariosLeads2 = new ArrayList<GrupoUsuariosLead>();
			List<Long> leadsId = new ArrayList<Long>();
			for(GrupoUsuariosPrincipals g:l.getGruposUsuarios())
			{
				List<GrupoUsuariosLead> grupoUsuariosLeads = grupoUsuariosLeadListProducer.retrieveAllGrupoUsuariosLeadByGrupoUsuarios(g.getGrupoUsuarios());
				grupoUsuariosLeads2.addAll(grupoUsuariosLeads);
			}
			for(GrupoUsuariosLead g:grupoUsuariosLeads2)
			{
				leadsId.add(g.getLead().getId());
			}
			if(leadsId.size() > 0)
			{
//				criteria.select(leadRoot).where(leadRoot.get("id").in(leadsId)).orderBy(cb.asc(leadRoot.get("nome")));
				leads = em.createNamedQuery("Lead.findByIds").setParameter("leads", leadsId).getResultList();
			}
			else
			{
//				criteria.select(leadRoot).where(cb.equal(leadRoot.get("id"), -1l)).orderBy(cb.asc(leadRoot.get("nome")));
				leads = new ArrayList<>();
			}
		}
//		leads = em.createQuery(criteria).getResultList();
	}
}
