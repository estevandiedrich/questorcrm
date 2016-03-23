package br.com.questor.crm.data;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.questor.crm.model.ContatoEmail;
import br.com.questor.crm.model.Email;

@RequestScoped
public class ContatoEmailListProducer {
	@Inject
	private EntityManager em;
	
//	private List<ContatoEmail> contatosEmail;
//	
//	@Produces
//	@Named
//	public List<ContatoEmail> getContatoEmails()
//	{
//		return contatosEmail;
//	}
//	
//	public void onContatoEmailListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final ContatoEmail contatoEmail) {
//		retrieveAllContatoEmailsOrderedByNome();
//	}
//	@PostConstruct
//	public void retrieveAllContatoEmailsOrderedByNome() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<ContatoEmail> criteria = cb.createQuery(ContatoEmail.class);
//		Root<ContatoEmail> contato = criteria.from(ContatoEmail.class);
//		criteria.select(contato).orderBy(cb.asc(contato.get("nome")));
//		contatosEmail = em.createQuery(criteria).getResultList();
//	}
	
	public List<ContatoEmail> retrieveAllContatoEmailsByEmailOrderedByNome(Email email) {
		return em.createNamedQuery("ContatoEmail.findByEmail").setParameter("email", email.getId()).getResultList();
	}
}
