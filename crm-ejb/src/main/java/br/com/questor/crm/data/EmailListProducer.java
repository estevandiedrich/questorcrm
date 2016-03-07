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

import br.com.questor.crm.model.Email;
import br.com.questor.crm.model.Lead;

@RequestScoped
public class EmailListProducer {
	@Inject
	private EntityManager em;
	
	private List<Email> emails;
	
	@Produces
	@Named
	public List<Email> getEmails()
	{
		return emails;
	}
	
	public void onEmailListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Email email) {
		retrieveAllEmailsOrderedBySentDate();
	}
	public List<Email> retrieveAllEmailsByLeadOrderedBySentDate(Lead lead) 
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Email> criteria = cb.createQuery(Email.class);
		Root<Email> email = criteria.from(Email.class);
		criteria.select(email).where(cb.equal(email.get("lead"), lead)).orderBy(cb.asc(email.get("sentDate")));
		return em.createQuery(criteria).getResultList();
	}
	@PostConstruct
	public void retrieveAllEmailsOrderedBySentDate() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Email> criteria = cb.createQuery(Email.class);
		Root<Email> email = criteria.from(Email.class);
		criteria.select(email).orderBy(cb.asc(email.get("sentDate")));
		emails = em.createQuery(criteria).getResultList();
	}
}
