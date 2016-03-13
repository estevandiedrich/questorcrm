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

import br.com.questor.crm.model.UF;

@RequestScoped
public class UFListProducer {
	@Inject
	private EntityManager em;
	
	private List<UF> ufs;
	
	@Produces
	@Named
	public List<UF> getUfs()
	{
		return ufs;
	}
	
	public void onUFListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final UF uf) {
		retrieveAllUFsOrderedBySigla();
	}
	@PostConstruct
	public void retrieveAllUFsOrderedBySigla() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UF> criteria = cb.createQuery(UF.class);
		Root<UF> uf = criteria.from(UF.class);
		criteria.select(uf).orderBy(cb.asc(uf.get("sigla")));
		ufs = em.createQuery(criteria).getResultList();
	}
}