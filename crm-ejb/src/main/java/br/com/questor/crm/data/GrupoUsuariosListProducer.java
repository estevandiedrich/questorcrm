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

import br.com.questor.crm.model.GrupoUsuarios;

@RequestScoped
public class GrupoUsuariosListProducer {
	@Inject
	private EntityManager em;
	
	private List<GrupoUsuarios> gruposUsuarios;
	
	@Produces
	@Named
	public List<GrupoUsuarios> getGruposUsuarios()
	{
		return gruposUsuarios;
	}
	
	public void onGrupoUsuariosListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final GrupoUsuarios grupoUsuarios) {
		retrieveAllGruposUsuariosOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllGruposUsuariosOrderedByNome() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<GrupoUsuarios> criteria = cb.createQuery(GrupoUsuarios.class);
//		Root<GrupoUsuarios> grupoUsuarios = criteria.from(GrupoUsuarios.class);
//		criteria.select(grupoUsuarios).orderBy(cb.asc(grupoUsuarios.get("descricao")));
//		gruposUsuarios = em.createQuery(criteria).getResultList();
		gruposUsuarios = em.createNamedQuery("GrupoUsuarios.findAll").getResultList();
	}
	
//	public List<GrupoUsuarios> retrieveAllGruposUsuariosByLeadOrderedByNome(Lead lead) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<GrupoUsuarios> criteria = cb.createQuery(GrupoUsuarios.class);
//		Root<GrupoUsuarios> grupoUsuarios = criteria.from(GrupoUsuarios.class);
//		criteria.select(grupoUsuarios).where(cb.equal(grupoUsuarios.get("lead"), lead)).orderBy(cb.asc(grupoUsuarios.get("descricao")));
//		return em.createQuery(criteria).getResultList();
//	}
//	public List<GrupoUsuarios> retrieveAllGruposUsuariosByPrincipalsOrderedByNome(Principals principals) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<GrupoUsuarios> criteria = cb.createQuery(GrupoUsuarios.class);
//		Root<GrupoUsuarios> grupoUsuarios = criteria.from(GrupoUsuarios.class);
//		criteria.select(grupoUsuarios).where(cb.equal(grupoUsuarios.get("principals"), principals)).orderBy(cb.asc(grupoUsuarios.get("descricao")));
//		return em.createQuery(criteria).getResultList();
//	}
}
