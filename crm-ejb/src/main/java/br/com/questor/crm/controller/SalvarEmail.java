package br.com.questor.crm.controller;

import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;

import br.com.questor.crm.model.Email;
import br.com.questor.crm.model.Lead;

@Stateful
@Model
public class SalvarEmail {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private LoginBean loginBean;
	
	@Inject
	private Event<Email> emailEventSrc;
	
	@Resource(lookup = "java:jboss/mail/crm-mail")
	private Session mailSession;
	
	private Email newEmail;
	
	@Produces
	@Named
	public Email getNewEmail() {
		return newEmail;
	}
	@Asynchronous //Metodo Assíncrono para que a aplicação continue normalmente sem ficar bloqueada até que o email seja enviado    
	 public void sendEmail(Email newEmail) {

        log.info("Email enviado por " + newEmail.getEmailFrom() + " para " + newEmail.getEmailTo() + " : " + newEmail.getSubject());
        try {
           //Criação de uma mensagem simples
            Message message = new MimeMessage(mailSession);
            //Cabeçalho do Email
            message.setFrom(new InternetAddress(newEmail.getEmailFrom()));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(newEmail.getEmailTo()));            
            message.setSubject(newEmail.getSubject());
            //Corpo do email
            message.setText(newEmail.getText());

            //Envio da mensagem            
            Transport.send(message);
            log.info("Email enviado");
        } catch (MessagingException e) {
            log.info("Erro a enviar o email : " + e.getMessage());        
        }
    }
	public void salvar(Lead lead) throws Exception {
		log.info("Salvando Email" + newEmail.getEmailTo());
		lead.getEmails().add(newEmail);
		newEmail.setEmailFrom(loginBean.getPrincipalsFromDB().getEmail());
		newEmail.setEmailTo(lead.getEmail());
		newEmail.setLead(lead);
		newEmail.setSentDate(new Date());
		em.persist(newEmail);
		emailEventSrc.fire(newEmail);
		this.sendEmail(newEmail);
		initNewEmail();
	}

	@PostConstruct
	public void initNewEmail() {
		newEmail = new Email();
	}
}