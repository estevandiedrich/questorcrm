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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import br.com.questor.crm.controller.LoginBean;
import br.com.questor.crm.model.Principals;

@RequestScoped
public class PrincipalsListProducer {
	@Inject
	private EntityManager em;
	
	private List<Principals> principals;
	
	@Produces
	@Named
	public List<Principals> getPrincipals()
	{
		return principals;
	}
	
	@Inject
	private LoginBean loginBean;
	
	public void onClienteListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Principals principals) {
		retrieveAllPrincipalsOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllPrincipalsOrderedByNome() {
		Metamodel metamodel = em.getMetamodel();
	    EntityType<Principals> entityPrincipals_ = metamodel.entity(Principals.class);
		Principals p = this.loginBean.getPrincipalsFromDB();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Principals> criteria = cb.createQuery(Principals.class);
		Root<Principals> principal = criteria.from(entityPrincipals_);
		Join role = principal.join(entityPrincipals_.getSingularAttribute("Role"), JoinType.INNER);
		if(loginBean.isCallerInRole("ADMIN"))
		{
			criteria.select(principal).orderBy(cb.asc(principal.get("PrincipalID")));
		}
		else
		{
			criteria.select(principal).where(principal.get("grupoUsuarios").in(p.getGruposUsuarios())).orderBy(cb.asc(principal.get("PrincipalID")));
		}
		
		principals = em.createQuery(criteria).getResultList();
	}
}
