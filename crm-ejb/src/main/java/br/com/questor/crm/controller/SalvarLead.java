package br.com.questor.crm.controller;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.commons.io.IOUtils;

import br.com.questor.crm.data.EmailListProducer;
import br.com.questor.crm.model.Contato;
import br.com.questor.crm.model.Email;
import br.com.questor.crm.model.GrupoUsuarios;
import br.com.questor.crm.model.Imagem;
import br.com.questor.crm.model.Lead;

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
	private EmailListProducer emailListProducer;
	
//	@Inject
//	private CotacaoListProducer cotacaoListProducer;
	
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
		for(Contato contato:newLead.getContatos())
		{
			contato.setLead(newLead);
			em.merge(contato);
		}
		for(GrupoUsuarios grupoUsuarios:newLead.getGruposUsuarios())
		{
			grupoUsuarios.setLead(newLead);
			em.merge(grupoUsuarios);
		}
//		if(newLead.getGrupoUsuarios().getId() == null)
//		{
//			Principals principal = loginBean.getPrincipalsFromDB();
//			newLead.setGrupoUsuarios(principal.getGrupoUsuarios());
//		}
//		if(newLead.getGrupo() == null || newLead.getGrupo().getId() == null)
//		{
//			newLead.setGrupo(null);
//		}
//		for(Cotacao cotacao:newLead.getCotacoes())
//		{
//			for(Negociacao negociacao:cotacao.getNegociacoes())
//			{
//				em.persist(negociacao);
//			}
//			em.persist(cotacao);
//		}
		em.persist(newLead);
		leadEventSrc.fire(newLead);
		initNewLead();
	}
	public void adicionarContato(String id)
	{
		if(id != null && !"".equalsIgnoreCase(id))
		{
			Contato contato = em.find(Contato.class, Long.parseLong(id));
			newLead.getContatos().add(contato);
		}
	}
	public void adicionarGrupoUsuarios(String id)
	{
		if(id != null && !"".equalsIgnoreCase(id))
		{
			GrupoUsuarios grupoUsuarios = em.find(GrupoUsuarios.class, Long.parseLong(id));
			newLead.getGruposUsuarios().add(grupoUsuarios);
		}
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
//		this.id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if(this.id != null && "".equalsIgnoreCase(this.id))
		{
			newLead = em.find(Lead.class, Long.parseLong(this.id));
			if(!Persistence.getPersistenceUtil().isLoaded(newLead, "emails"))
			{
				List<Email> emails = emailListProducer.retrieveAllEmailsByLeadOrderedBySentDate(newLead);
				newLead.setEmails(emails);
//				List<Cotacao> cotacoes = cotacaoListProducer.retrieveAllCotacoesByLeadOrderedByDataEHoraCriacao(newLead);
//				for(Cotacao cotacao:cotacoes)
//				{
//					List<Negociacao> negociacoes = negociacaoListProducer.retrieveAllNegociacoesByCotacaoOrderedByDataEHora(cotacao);
//					cotacao.setNegociacoes(negociacoes);
//				}
//				newLead.setCotacoes(cotacoes);
			}
			log.info(newLead.getEmails().size()+ " emails");
		}
		else
		{
			newLead = new Lead();
		}
	}
}
