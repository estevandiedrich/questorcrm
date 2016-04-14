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

import br.com.questor.crm.model.Principals;
import br.com.questor.crm.model.Roles;

@RequestScoped
public class RolesListProducer {
	@Inject
	private EntityManager em;
	
	private List<Roles> roles;
	
	@Produces
	@Named
	public List<Roles> getRoles()
	{
		return roles;
	}
	public void onClienteListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Roles roles) {
		retrieveAllRolesOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllRolesOrderedByNome() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Roles> criteria = cb.createQuery(Roles.class);
		Root<Roles> role = criteria.from(Roles.class);
		criteria.select(role).orderBy(cb.asc(role.get("Role")));
		roles = em.createQuery(criteria).getResultList();
	}
	
	public Roles retrieveAllRolesByPrincipalOrderedByNome(Principals principal) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Roles> criteria = cb.createQuery(Roles.class);
		Root<Roles> role = criteria.from(Roles.class);
		criteria.select(role).where(cb.equal(role.get("PrincipalID"), principal.getPrincipalId()));
		return em.createQuery(criteria).getSingleResult();
	}
}
