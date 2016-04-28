package br.com.questor.crm.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.model.GrupoUsuarios;

@Stateful
//@Model
@SessionScoped
@Named
public class SalvarGrupoUsuarios {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<GrupoUsuarios> grupoUsuariosEventSrc;
	
	private GrupoUsuarios newGrupoUsuarios;
	
	@Produces
	@Named
	public GrupoUsuarios getNewGrupoUsuarios() {
		return newGrupoUsuarios;
	}
	public String novo()
	{
		initNewGrupoUsuarios();
		return "/pages/protected/admin/grupousuarios?faces-redirect=true";
	}
	public String editar(GrupoUsuarios grupoUsuarios)
	{
		newGrupoUsuarios = grupoUsuarios;
		return "/pages/protected/admin/grupousuarios?faces-redirect=true";
	}
	public void excluir(GrupoUsuarios grupoUsuarios)
	{
		log.info("Excluindo Cargo " + grupoUsuarios.getDescricao());
		em.remove(em.contains(grupoUsuarios) ? grupoUsuarios:em.merge(grupoUsuarios));
		grupoUsuariosEventSrc.fire(grupoUsuarios);
		initNewGrupoUsuarios();
	}
	public void salvar() throws Exception {
		log.info("Salvando Grupo Usuarios" + newGrupoUsuarios.getDescricao());
		if(newGrupoUsuarios.getId() == null)
		{
			em.persist(newGrupoUsuarios);
		}
		else
		{
			em.merge(newGrupoUsuarios);
		}
		grupoUsuariosEventSrc.fire(newGrupoUsuarios);
		initNewGrupoUsuarios();
	}

	@PostConstruct
	public void initNewGrupoUsuarios() {
		newGrupoUsuarios = new GrupoUsuarios();
	}
}
