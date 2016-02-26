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
		Principals p = this.loginBean.getPrincipalsFromDB();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Principals> criteria = cb.createQuery(Principals.class);
		Root<Principals> principal = criteria.from(Principals.class);
		if(loginBean.isCallerInRole("ADMIN"))
		{
			criteria.select(principal).orderBy(cb.asc(principal.get("PrincipalID")));
		}
		else
		{
			criteria.select(principal).where(cb.equal(principal.get("grupoUsuarios"), p.getGrupoUsuarios())).orderBy(cb.asc(principal.get("PrincipalID")));
		}
		
		principals = em.createQuery(criteria).getResultList();
	}
}
