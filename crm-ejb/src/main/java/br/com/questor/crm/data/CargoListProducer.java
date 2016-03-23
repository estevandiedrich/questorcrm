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

import br.com.questor.crm.model.Cargo;

@RequestScoped
public class CargoListProducer {
	@Inject
	private EntityManager em;
	
	private List<Cargo> cargos;
	
	@Produces
	@Named
	public List<Cargo> getCargos()
	{
		return cargos;
	}
	
	public void onCargoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Cargo cargo) {
		retrieveAllCargosOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllCargosOrderedByNome() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Cargo> criteria = cb.createQuery(Cargo.class);
//		Root<Cargo> cargo = criteria.from(Cargo.class);
//		criteria.select(cargo).orderBy(cb.asc(cargo.get("descricao")));
//		cargos = em.createQuery(criteria).getResultList();
//		@SuppressWarnings(value = "unchecked")
		cargos = em.createNamedQuery("Cargo.findAll").getResultList();
	}
}
