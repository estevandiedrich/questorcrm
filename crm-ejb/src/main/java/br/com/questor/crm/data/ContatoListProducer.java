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

import br.com.questor.crm.model.Contato;
import br.com.questor.crm.model.Lead;

@RequestScoped
public class ContatoListProducer {
	@Inject
	private EntityManager em;
	
	private List<Contato> contatos;
	
	@Produces
	@Named
	public List<Contato> getContatos()
	{
		return contatos;
	}
	
	public void onContatoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Contato contato) {
		retrieveAllContatosOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllContatosOrderedByNome() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Contato> criteria = cb.createQuery(Contato.class);
		Root<Contato> contato = criteria.from(Contato.class);
		criteria.select(contato).orderBy(cb.asc(contato.get("nome")));
		contatos = em.createQuery(criteria).getResultList();
	}
	
	public List<Contato> retrieveAllContatosByLeadOrderedByNome(Lead lead) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Contato> criteria = cb.createQuery(Contato.class);
//		Root<Contato> contato = criteria.from(Contato.class);
//		criteria.select(contato).where(cb.equal(contato.get("lead"), lead)).orderBy(cb.asc(contato.get("nome")));
////		contatos = em.createQuery(criteria).getResultList();
//		return em.createQuery(criteria).getResultList();
		return em.createNamedQuery("Contato.findByLead").setParameter("lead", lead.getId()).getResultList();
	}
	public Contato findById(Long id)
	{
		return (Contato)em.createNamedQuery("Contato.findById").setParameter("id", id).getSingleResult();
	}
}
