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

import br.com.questor.crm.model.GrupoUsuarios;
import br.com.questor.crm.model.GrupoUsuariosLead;
import br.com.questor.crm.model.Lead;

@RequestScoped
public class GrupoUsuariosLeadListProducer {
	@Inject
	private EntityManager em;
	
	private List<GrupoUsuariosLead> grupoUsuariosLead;
	
	@Produces
	@Named
	public List<GrupoUsuariosLead> getGrupoUsuariosLeads()
	{
		return grupoUsuariosLead;
	}
	
	public void onGrupoUsuariosLeadListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final GrupoUsuariosLead grupoUsuariosLead) {
		retrieveAllGrupoUsuariosLead();
	}
	@PostConstruct
	public void retrieveAllGrupoUsuariosLead() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GrupoUsuariosLead> criteria = cb.createQuery(GrupoUsuariosLead.class);
		Root<GrupoUsuariosLead> grupoUsuariosLeadRoot = criteria.from(GrupoUsuariosLead.class);
		criteria.select(grupoUsuariosLeadRoot);
		grupoUsuariosLead = em.createQuery(criteria).getResultList();
	}
	public List<GrupoUsuariosLead> retrieveAllGrupoUsuariosLeadByLead(Lead lead) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GrupoUsuariosLead> criteria = cb.createQuery(GrupoUsuariosLead.class);
		Root<GrupoUsuariosLead> grupoUsuariosLeadRoot = criteria.from(GrupoUsuariosLead.class);
		criteria.select(grupoUsuariosLeadRoot).where(cb.equal(grupoUsuariosLeadRoot.get("lead"), lead));
		return em.createQuery(criteria).getResultList();
	}
	public List<GrupoUsuariosLead> retrieveAllGrupoUsuariosLeadByGrupoUsuarios(GrupoUsuarios grupoUsuarios) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<GrupoUsuariosLead> criteria = cb.createQuery(GrupoUsuariosLead.class);
//		Root<GrupoUsuariosLead> grupoUsuariosLeadRoot = criteria.from(GrupoUsuariosLead.class);
//		criteria.select(grupoUsuariosLeadRoot).where(cb.equal(grupoUsuariosLeadRoot.get("grupoUsuarios"), grupoUsuarios));
//		return em.createQuery(criteria).getResultList();
		return em.createNamedQuery("GrupoUsuariosLead.findByGrupoUsuarios").setParameter("grupoUsuarios", grupoUsuarios).getResultList();
	}
}
