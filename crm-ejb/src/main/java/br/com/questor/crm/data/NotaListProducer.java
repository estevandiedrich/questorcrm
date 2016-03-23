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

import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.Nota;

@RequestScoped
@Named
public class NotaListProducer {
	@Inject
	private EntityManager em;
	
	private List<Nota> notas;
	
	@Produces
	@Named
	public List<Nota> getNotas()
	{
		return notas;
	}
	
	public void onNotaListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Nota nota) {
		retrieveAllNotasOrderedByDataEHora();
	}
	
	public List<Nota> retrieveAllNotasByLeadOrderedByDescricao(Lead lead)
	{
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Nota> criteria = cb.createQuery(Nota.class);
//		Root<Nota> nota = criteria.from(Nota.class);
//		criteria.select(nota).where(cb.equal(nota.get("lead"), lead)).orderBy(cb.desc(nota.get("dataEHora")));
//		return em.createQuery(criteria).getResultList();
		return em.createNamedQuery("Nota.findByLead").setParameter("lead", lead.getId()).getResultList();
	}
	
	@PostConstruct
	public void retrieveAllNotasOrderedByDataEHora() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Nota> criteria = cb.createQuery(Nota.class);
//		Root<Nota> nota = criteria.from(Nota.class);
//		criteria.select(nota).orderBy(cb.desc(nota.get("dataEHora")));
//		notas = em.createQuery(criteria).getResultList();
		notas = em.createNamedQuery("Nota.findAll").getResultList();
	}
}
