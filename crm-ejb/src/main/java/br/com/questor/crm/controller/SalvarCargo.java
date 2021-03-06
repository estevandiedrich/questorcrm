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

import br.com.questor.crm.model.Cargo;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarCargo {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Cargo> cargoEventSrc;
	
	private Cargo newCargo;
	
	@Produces
	@Named
	public Cargo getNewCargo() {
		return newCargo;
	}
	public String novo()
	{
		initNewCargo();
		return "/pages/protected/admin/cargo?faces-redirect=true";
	}
	public String editar(Cargo cargo)
	{
		newCargo = cargo;
		return "/pages/protected/admin/cargo?faces-redirect=true";
	}
	public void salvar() throws Exception {
		log.info("Salvando Cargo " + newCargo.getDescricao());
		if(newCargo.getId() == null)
		{
			em.persist(newCargo);
		}
		else
		{
			em.merge(newCargo);
		}
		cargoEventSrc.fire(newCargo);
		initNewCargo();
	}
	public void excluir(Cargo cargo)
	{
		log.info("Excluindo Cargo " + cargo.getDescricao());
		em.remove(em.contains(cargo) ? cargo:em.merge(cargo));
		cargoEventSrc.fire(cargo);
		initNewCargo();
	}
	@PostConstruct
	public void initNewCargo() {
		newCargo = new Cargo();
	}
}
