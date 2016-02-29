package br.com.questor.crm.controller;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.commons.io.IOUtils;

import br.com.questor.crm.data.CotacaoListProducer;
import br.com.questor.crm.data.EmailListProducer;
import br.com.questor.crm.data.NegociacaoListProducer;
import br.com.questor.crm.model.Cotacao;
import br.com.questor.crm.model.Email;
import br.com.questor.crm.model.Imagem;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.Principals;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarLead extends BaseController implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4569472491388568672L;

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private LoginBean loginBean;
	
	@Inject
	private EmailListProducer emailListProducer;
	
	@Inject
	private CotacaoListProducer cotacaoListProducer;
	
	@Inject
	private NegociacaoListProducer negociacaoListProducer;
		
	@Inject
	private Event<Lead> leadEventSrc;
	
	private String id;
	
	private Lead newLead;
	
	@Produces
	@Named
	public Lead getNewLead() {
		return newLead;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando Lead" + newLead.getNome());
		if(newLead.getImagemPart() != null)
		{
			Imagem imagem = new Imagem();
			imagem.setNome(newLead.getImagemPart().getName());
			imagem.setSize(newLead.getImagemPart().getSize());
			imagem.setContentType(newLead.getImagemPart().getContentType());
			imagem.setImagem(IOUtils.toByteArray(newLead.getImagemPart().getInputStream()));
			em.persist(imagem);
			newLead.setImagem(imagem);
		}
		if(newLead.getGrupoUsuarios().getId() == null)
		{
			Principals principal = loginBean.getPrincipalsFromDB();
			newLead.setGrupoUsuarios(principal.getGrupoUsuarios());
		}
		if(newLead.getGrupo() == null || newLead.getGrupo().getId() == null)
		{
			newLead.setGrupo(null);
		}
//		for(Cotacao cotacao:newLead.getCotacoes())
//		{
//			for(Negociacao negociacao:cotacao.getNegociacoes())
//			{
//				em.persist(negociacao);
//			}
//			em.persist(cotacao);
//		}
		em.merge(newLead);
		leadEventSrc.fire(newLead);
		initNewLead();
	}
	
	public String detalhar()
	{
		initNewLead();
		return "/pages/protected/user/detalharlead";
	}
	
	public String novo()
	{
		initNewLead();
		return "/pages/protected/user/leads";
	}

	@PostConstruct
	public void initNewLead() {
		this.id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if(this.id != null && this.id != "")
		{
			newLead = em.find(Lead.class, Long.parseLong(this.id));
			if(!Persistence.getPersistenceUtil().isLoaded(newLead, "emails"))
			{
				List<Email> emails = emailListProducer.retrieveAllEmailsByLeadOrderedBySentDate(newLead);
				newLead.setEmails(emails);
				List<Cotacao> cotacoes = cotacaoListProducer.retrieveAllCotacoesByLeadOrderedByDataEHoraCriacao(newLead);
//				for(Cotacao cotacao:cotacoes)
//				{
//					List<Negociacao> negociacoes = negociacaoListProducer.retrieveAllNegociacoesByCotacaoOrderedByDataEHora(cotacao);
//					cotacao.setNegociacoes(negociacoes);
//				}
				newLead.setCotacoes(cotacoes);
			}
			log.info(newLead.getEmails().size()+ " emails");
		}
		else
		{
			newLead = new Lead();
		}
	}
}
