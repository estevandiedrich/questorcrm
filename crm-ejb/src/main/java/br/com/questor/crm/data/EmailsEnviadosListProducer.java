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

import br.com.questor.crm.controller.LoginBean;
import br.com.questor.crm.model.Email;

@RequestScoped
public class EmailsEnviadosListProducer {
	@Inject
	private EntityManager em;
	@Inject
	private LoginBean loginBean;
	
	private List<Email> emailsEnviadosPorMim;
	
	@Produces
	@Named
	public List<Email> getEmailsEnviadosPorMim()
	{
		return emailsEnviadosPorMim;
	}
	
	public void onEmailListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Email email) {
		retrieveAllEmailsOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllEmailsOrderedByNome() {
		emailsEnviadosPorMim = em.createNamedQuery("Email.findByEmail").setParameter("email", loginBean.getPrincipalsFromDB().getPrincipalID()).getResultList();
	}
}
