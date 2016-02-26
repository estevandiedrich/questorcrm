package br.com.questor.crm.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.model.GrupoUsuarios;

@Stateful
@Model
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
	
	public void salvar() throws Exception {
		log.info("Salvando Grupo Usuarios" + newGrupoUsuarios.getDescricao());
		em.persist(newGrupoUsuarios);
		grupoUsuariosEventSrc.fire(newGrupoUsuarios);
		initNewGrupoUsuarios();
	}

	@PostConstruct
	public void initNewGrupoUsuarios() {
		newGrupoUsuarios = new GrupoUsuarios();
	}
}
