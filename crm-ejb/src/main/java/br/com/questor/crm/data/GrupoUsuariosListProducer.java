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
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GrupoUsuarios> criteria = cb.createQuery(GrupoUsuarios.class);
		Root<GrupoUsuarios> grupoUsuarios = criteria.from(GrupoUsuarios.class);
		criteria.select(grupoUsuarios).orderBy(cb.asc(grupoUsuarios.get("descricao")));
		gruposUsuarios = em.createQuery(criteria).getResultList();
	}
}
