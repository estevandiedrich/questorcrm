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
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import br.com.questor.crm.controller.LoginBean;
import br.com.questor.crm.model.GrupoUsuarios;
import br.com.questor.crm.model.GrupoUsuariosPrincipals;
import br.com.questor.crm.model.Principals;
import br.com.questor.crm.model.Roles;

@RequestScoped
public class PrincipalsListProducer {
	@Inject
	private EntityManager em;
	
	private List<Principals> principals;
	
	private List<Principals> participantesInternos;
	
	@Inject
	private RolesListProducer rolesListProducer;
	
	@Produces
	@Named
	public List<Principals> getPrincipals()
	{
		return principals;
	}
	
	@Produces
	@Named
	public List<Principals> getParticipantesInternos()
	{
		return participantesInternos;
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
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Principals> criteria = cb.createQuery(Principals.class);
		Root<Principals> principal = criteria.from(entityPrincipals_);
//		Join role = principal.join(entityPrincipals_.getSingularAttribute("Role"), JoinType.INNER);
		if(loginBean.isCallerInRole("ADMIN"))
		{
			criteria.select(principal).orderBy(cb.asc(principal.get("PrincipalID")));
			principals = em.createQuery(criteria).getResultList();
			for(Principals pr:principals)
			{
				Roles role = rolesListProducer.retrieveAllRolesByPrincipalOrderedByNome(pr);
				pr.setRole(role);
			}
		}
		Principals p = loginBean.getPrincipalsFromDB();
		List<Long> gruposUsuariosId = new ArrayList<>();
		List<GrupoUsuarios> gruposUsuarios = em.createNamedQuery("GrupoUsuariosPrincipals.findGrupoUsuariosByPrincipal").setParameter("principal", p.getId()).getResultList();
		for(GrupoUsuarios grupoUsuarios:gruposUsuarios)
		{
			gruposUsuariosId.add(grupoUsuarios.getId());
		}
		if(gruposUsuariosId != null && gruposUsuariosId.size() > 0)
		{
			participantesInternos = em.createNamedQuery("GrupoUsuariosPrincipals.findByGrupoUsuarios").setParameter("gruposUsuarios", gruposUsuariosId).getResultList();
		}
//		else
//		{
//			List<GrupoUsuarios> gruposUsuarios = grupoUsuariosListProducer.retrieveAllGruposUsuariosByPrincipalsOrderedByNome(p);
//			p.setGruposUsuarios(gruposUsuarios);
//			criteria.select(principal).where(principal.get("gruposUsuarios").in(p.getGruposUsuarios())).orderBy(cb.asc(principal.get("PrincipalID")));
//			Principals pr = em.createQuery(criteria).getSingleResult();
//			Roles role = rolesListProducer.retrieveAllRolesByPrincipalOrderedByNome(pr);
//			pr.setRole(role);
//			principals = new ArrayList<Principals>();
//			principals.add(pr);
//		}
	}
	public Principals findById(Long id)
	{
		Principals participanteInterno = (Principals)em.createNamedQuery("Principals.findById").setParameter("id", id).getSingleResult();
		return participanteInterno;
	}
}
