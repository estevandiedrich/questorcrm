package br.com.questor.crm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.security.auth.spi.Util;

import br.com.questor.crm.data.ContatoEmailListProducer;
import br.com.questor.crm.data.ContatoListProducer;
import br.com.questor.crm.data.EmailListProducer;
import br.com.questor.crm.model.Anexo;
import br.com.questor.crm.model.Contato;
import br.com.questor.crm.model.ContatoEmail;
import br.com.questor.crm.model.Email;
import br.com.questor.crm.model.Imagem;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.Principals;
import br.com.questor.crm.model.Propriedades;

@Stateful
//@Model
@Named
@SessionScoped
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
	
	@Inject
	private ContatoListProducer contatoListProducer;
	
	@Inject
	private ContatoEmailListProducer contatoEmailListProducer;
	
	@Inject
	private EmailListProducer emailListProducer;
	
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
            String[] destinatarios = new String[newEmail.getEmailTo().size()];
            for(int i = 0;i < newEmail.getEmailTo().size();i++)
            {   
            	ContatoEmail contatoEmail = newEmail.getEmailTo().get(i);
            	destinatarios[i] = contatoEmail.getContato().getEmail();
            }
            String emailTo = Arrays.toString(destinatarios).replace("[", "").replace("]","");
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailTo));            
            message.setSubject(newEmail.getSubject());
            //Corpo do email
            message.setText(newEmail.getText());
            
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Anexos");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            for(Anexo anexo:newEmail.getAnexos())
            {
            	addAttachment(multipart, anexo.getImagem());
            }
            message.setContent(multipart);
            
            //Envio da mensagem            
            Transport.send(message);
            log.info("Email enviado");
        } catch (MessagingException e) {
            log.info("Erro a enviar o email : " + e.getMessage());        
        }
    }
	private static void addAttachment(Multipart multipart, Imagem imagem)
	{
//	    DataSource source = new FileDataSource(filename);
		DataHandler source = new DataHandler(imagem.getImagem(),imagem.getContentType());
	    BodyPart messageBodyPart = new MimeBodyPart();        
	    try {
			messageBodyPart.setDataHandler(source);
			messageBodyPart.setFileName(imagem.getNome());
			multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void adicionarContato(){
		ContatoEmail contatoEmail = new ContatoEmail();
		Contato contato = contatoListProducer.findById(newEmail.getContatoSelecionado().getId());
		contatoEmail.setContato(contato);
		newEmail.getEmailTo().add(contatoEmail);
	}
	public void enviarEmailNovoUsuario(Principals principals)
	{
		ContatoEmail contatoEmail = new ContatoEmail();
		Contato contato = new Contato();
		contato.setEmail(principals.getPrincipalID());
		contatoEmail.setContato(contato);
		List<ContatoEmail> contatoEmailList = new ArrayList<>();
		contatoEmailList.add(contatoEmail);
		Propriedades emailRecuperacaoSenha = (Propriedades) em.createNamedQuery("Propriedades.findByChave").setParameter("chave", Propriedades.EMAIL_NOVO_USUARIO).getSingleResult();
		Email novoUsuario = new Email();
		novoUsuario.setEmailFrom(emailRecuperacaoSenha.getValor());
		novoUsuario.setEmailTo(contatoEmailList);
		novoUsuario.setSubject("Bem vindo ao Questor CRM");
		novoUsuario.setText("http://www.questores.com.br/crm Senha temporária "+principals.getPassword());
		sendEmail(novoUsuario);
	}
	public void esqueceuSenha(String email)
	{
		try
		{
			Principals esquecido = (Principals) em.createNamedQuery("Principals.findByEmail").setParameter("email", email).getSingleResult();
			ContatoEmail contatoEmail = new ContatoEmail();
			Contato contato = new Contato();
			contato.setEmail(esquecido.getPrincipalID());
			contatoEmail.setContato(contato);
			List<ContatoEmail> contatoEmailList = new ArrayList<>();
			contatoEmailList.add(contatoEmail);
			Propriedades emailRecuperacaoSenha = (Propriedades) em.createNamedQuery("Propriedades.findByChave").setParameter("chave", Propriedades.EMAIL_RECUPERACAO_SENHA).getSingleResult();
			Email recuperaSenha = new Email();
			recuperaSenha.setEmailFrom(emailRecuperacaoSenha.getValor());
			recuperaSenha.setEmailTo(contatoEmailList);
			recuperaSenha.setSubject("Email para recuperação de senha");
			recuperaSenha.setText("http://www.questores.com.br/crm Senha temporária xyz");
			sendEmail(recuperaSenha);
			esquecido.setPassword(Util.createPasswordHash("SHA-256","BASE64",null, null, "xyz"));
			esquecido.setPrimeiroLogin(Boolean.TRUE);
			em.merge(esquecido);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Email enviado com sucesso.", null);
            FacesContext.getCurrentInstance().addMessage(null, m);
		}
		catch(NoResultException e)
		{
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi localizado usuário válido para o email fornecido.", null);
            FacesContext.getCurrentInstance().addMessage(null, m);
		}
	}
	public void salvar() throws Exception {
		log.info("Salvando Email" + newEmail.getEmailTo());
		if(newEmail.getId() == null)
		{
			newEmail.getLead().getEmails().add(newEmail);
			newEmail.setEmailFrom(loginBean.getPrincipalsFromDB().getPrincipalID());
			newEmail.setSentDate(new Date());
			em.persist(newEmail);
			for(ContatoEmail contatoEmail:newEmail.getEmailTo())
			{
				if(contatoEmail.getId() == null)
				{
					contatoEmail.setEmail(newEmail);
					em.persist(contatoEmail);
				}
			}
		}
		emailEventSrc.fire(newEmail);
		this.sendEmail(newEmail);
		initNewEmail();
	}
	
	public void setLead()
	{
		if(newEmail.getLead().getId() != null)
		{
			Lead lead = em.find(Lead.class, newEmail.getLead().getId());
			List<Contato> contatos = contatoListProducer.retrieveAllContatosByLeadOrderedByNome(lead);
			if(contatos == null)
				contatos = new ArrayList<Contato>();
			lead.setContatos(contatos);
			List<Email> emails = emailListProducer.retrieveAllEmailsByLeadOrderedBySentDate(lead);
			if(emails == null)
			{
				emails = new ArrayList<Email>();
			}
			else
			{
				for(Email email:emails)
				{
					email.setEmailTo(contatoEmailListProducer.retrieveAllContatoEmailsByEmailOrderedByNome(email));
				}
			}
			lead.setEmails(emails);
			newEmail.setLead(lead);
		}
	}

	@PostConstruct
	public void initNewEmail() {
		newEmail = new Email();
	}
}