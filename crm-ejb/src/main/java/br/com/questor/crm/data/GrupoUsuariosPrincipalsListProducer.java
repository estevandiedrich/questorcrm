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
import br.com.questor.crm.model.GrupoUsuariosPrincipals;
import br.com.questor.crm.model.Principals;

@RequestScoped
public class GrupoUsuariosPrincipalsListProducer {
	@Inject
	private EntityManager em;
	
	private List<GrupoUsuariosPrincipals> grupoUsuariosPrincipals;
	
	@Produces
	@Named
	public List<GrupoUsuariosPrincipals> getGrupoUsuariosPrincipalss()
	{
		return grupoUsuariosPrincipals;
	}
	
	public void onGrupoUsuariosPrincipalsListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final GrupoUsuariosPrincipals grupoUsuariosPrincipals) {
		retrieveAllGrupoUsuariosPrincipals();
	}
	@PostConstruct
	public void retrieveAllGrupoUsuariosPrincipals() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GrupoUsuariosPrincipals> criteria = cb.createQuery(GrupoUsuariosPrincipals.class);
		Root<GrupoUsuariosPrincipals> grupoUsuariosPrincipalsRoot = criteria.from(GrupoUsuariosPrincipals.class);
		criteria.select(grupoUsuariosPrincipalsRoot);
		grupoUsuariosPrincipals = em.createQuery(criteria).getResultList();
	}
	public List<GrupoUsuariosPrincipals> retrieveAllGrupoUsuariosPrincipalsByPrincipal(Principals principal) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GrupoUsuariosPrincipals> criteria = cb.createQuery(GrupoUsuariosPrincipals.class);
		Root<GrupoUsuariosPrincipals> grupoUsuariosPrincipalsRoot = criteria.from(GrupoUsuariosPrincipals.class);
		criteria.select(grupoUsuariosPrincipalsRoot).where(cb.equal(grupoUsuariosPrincipalsRoot.get("principals"), principal));
		return em.createQuery(criteria).getResultList();
	}
	public List<GrupoUsuariosPrincipals> retrieveAllGrupoUsuariosPrincipalsByGrupoUsuarios(GrupoUsuarios grupoUsuarios) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GrupoUsuariosPrincipals> criteria = cb.createQuery(GrupoUsuariosPrincipals.class);
		Root<GrupoUsuariosPrincipals> grupoUsuariosPrincipalsRoot = criteria.from(GrupoUsuariosPrincipals.class);
		criteria.select(grupoUsuariosPrincipalsRoot).where(cb.equal(grupoUsuariosPrincipalsRoot.get("grupoUsuarios"), grupoUsuarios));
		return em.createQuery(criteria).getResultList();
	}
}
