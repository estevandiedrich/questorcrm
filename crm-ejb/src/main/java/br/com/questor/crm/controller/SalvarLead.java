package br.com.questor.crm.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.questor.crm.data.AnexoListProducer;
import br.com.questor.crm.data.AtividadeAgendaListProducer;
import br.com.questor.crm.data.CidadeListProducer;
import br.com.questor.crm.data.ContatoListProducer;
import br.com.questor.crm.data.EmailListProducer;
import br.com.questor.crm.data.GrupoUsuariosLeadListProducer;
import br.com.questor.crm.data.GrupoUsuariosPrincipalsListProducer;
import br.com.questor.crm.data.NotaListProducer;
import br.com.questor.crm.data.OportunidadeListProducer;
import br.com.questor.crm.model.Anexo;
import br.com.questor.crm.model.Arquivo;
import br.com.questor.crm.model.Atividade;
import br.com.questor.crm.model.AtividadeAgenda;
import br.com.questor.crm.model.Cidade;
import br.com.questor.crm.model.Contato;
import br.com.questor.crm.model.Email;
import br.com.questor.crm.model.GrupoUsuarios;
import br.com.questor.crm.model.GrupoUsuariosLead;
import br.com.questor.crm.model.GrupoUsuariosPrincipals;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.Nota;
import br.com.questor.crm.model.Oportunidade;
import br.com.questor.crm.model.Origem;
import br.com.questor.crm.model.Principals;
import br.com.questor.crm.model.UF;

@Stateful
// @Model
@Named
@SessionScoped
public class SalvarLead implements Serializable {
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
	
	@Inject
	private LoginBean loginBean;

	@Inject
	private ContatoListProducer contatoListProducer;

	@Inject
	private AnexoListProducer anexoListProducer;
	
	@Inject
	private NotaListProducer notaListProducer;

	@Inject
	private GrupoUsuariosPrincipalsListProducer grupoUsuariosPrincipalsListProducer;
	
	@Inject
	private GrupoUsuariosLeadListProducer grupoUsuariosLeadListProducer;

	@Inject
	private AtividadeAgendaListProducer atividadeAgendaListProducer;
	
	@Inject
	private CidadeListProducer cidadeListProducer;
	
	@Inject
	private OportunidadeListProducer oportunidadesListProducer;

	@Inject
	private Event<Lead> leadEventSrc;

	private Lead newLead;
	
	private List<Oportunidade> oportunidades = new ArrayList<>();
	
	public List<Oportunidade> getOportunidades()
	{
		if(newLead != null && newLead.getId() != null)
		{
			oportunidades = oportunidadesListProducer.retrieveAllOportunidadesByLeadOrderedByNome(newLead);
		}
		return oportunidades;
	}

	@Produces
	@Named
	public Lead getNewLead() {
		return newLead;
	}
	public void excluir(Lead lead)
	{
		log.info("Excluindo Lead " + lead.getNome());
		em.remove(em.contains(lead) ? lead:em.merge(lead));
		leadEventSrc.fire(lead);
		initNewLead();
	}
	public void setUF()
	{
		if(newLead.getUf().getId() != null)
		{
			UF uf = em.find(UF.class, newLead.getUf().getId());
			List<Cidade> cidadesPorUf = cidadeListProducer.retrieveAllCidadesByUfOrderedByNome(uf);
			newLead.setCidadesPorUf(cidadesPorUf);
		}
	}
	public boolean imagemCarregada()
	{
		return newLead != null && newLead.getImagem() != null;
	}
	public void salvar() throws Exception {
		log.info("Salvando Lead" + newLead.getNome());
			if (newLead.getImagemPart() != null) {
				Arquivo imagem = new Arquivo();
				imagem.setNome(newLead.getImagemPart().getName());
				imagem.setSize(newLead.getImagemPart().getSize());
				imagem.setContentType(newLead.getImagemPart().getContentType());
				imagem.setContent(IOUtils.toByteArray(newLead.getImagemPart().getInputStream()));
				em.persist(imagem);
				newLead.setImagem(imagem);
			}
			if(newLead.getOrigem() != null && newLead.getOrigem().getId() == null)
			{
				newLead.setOrigem(null);
			}
			if(newLead.getAtividade() != null && newLead.getAtividade().getId() == null)
			{
				newLead.setAtividade(null);
			}
			if (newLead.getLeadPai() != null && newLead.getLeadPai().getId() == null) {
				newLead.setLeadPai(null);
			}
			if (newLead.getId() == null) {
				newLead.setUsuarioQueCadastrou(loginBean.getPrincipalsFromDB());
				em.persist(newLead);
			} else {
				em.merge(newLead);
			}
			for(Nota nota:newLead.getNotas())
			{
				if(nota.getId() == null)
				{
					nota.setLead(newLead);
					em.persist(nota);
				}
			}
			for (Anexo anexo : newLead.getAnexos()) {
				if(anexo.getId() == null)
				{
					anexo.setLead(newLead);
					em.persist(anexo.getArquivo());
					em.persist(anexo);
				}
			}
			for (Contato contato : newLead.getContatos()) {
				if(contato.getId() == null)
				{
					contato.setLead(newLead);
					em.persist(contato);
				}
			}
			for (GrupoUsuariosLead grupoUsuarios : newLead.getGruposUsuarios()) {
				if(grupoUsuarios.getId() == null)
				{
					grupoUsuarios.setLead(newLead);
					em.persist(grupoUsuarios);
				}
				else
				{
					grupoUsuarios.setLead(newLead);
					em.merge(grupoUsuarios);
				}
			}
			leadEventSrc.fire(newLead);
			initNewLead();
	}

	public void adicionarContato(String id) {
		if (id != null && !"".equalsIgnoreCase(id)) {
			Contato contato = em.find(Contato.class, Long.parseLong(id));
			newLead.getContatos().add(contato);
		}
	}
	public void removerGrupoUsuarios(GrupoUsuariosLead grupoUsuarios)
	{
		GrupoUsuariosLead remover = null;
		for(GrupoUsuariosLead grupoUsuariosLead:newLead.getGruposUsuarios())
		{
			if(grupoUsuariosLead.getGrupoUsuarios().getId() == grupoUsuarios.getGrupoUsuarios().getId())
			{
				remover = grupoUsuariosLead;
				break;
			}
		}
		newLead.getGruposUsuarios().remove(remover);
		if(remover.getId() != null)
		{
			em.remove(em.contains(remover) ? remover:em.merge(remover));
		}
	}
	public void adicionarGrupoUsuarios(String id) {
		if (id != null && !"".equalsIgnoreCase(id)) {
			GrupoUsuarios grupoUsuarios = em.find(GrupoUsuarios.class, Long.parseLong(id));
			GrupoUsuariosLead grupoUsuariosLead = new GrupoUsuariosLead();
			grupoUsuariosLead.setGrupoUsuarios(grupoUsuarios);
			newLead.getGruposUsuarios().add(grupoUsuariosLead);
		}
	}
	
	public String listagem()
	{
		return "/pages/protected/user/listagemleads?faces-redirect=true";
	}

	public String editar(Lead lead) {
		newLead = lead;
		editNewLead();
		return "/pages/protected/user/leads?faces-redirect=true";
	}

	public String novo() {
		initNewLead();
		return "/pages/protected/user/leads?faces-redirect=true";
	}
	public void editNewLead()
	{
		if (newLead != null && newLead.getId() != null) {
			if (newLead.getLeadPai() == null) {
				newLead.setLeadPai(new Lead());
			}
			if (!Persistence.getPersistenceUtil().isLoaded(newLead, "emails")) {
				List<Email> emails = emailListProducer.retrieveAllEmailsByLeadOrderedBySentDate(newLead);
				newLead.setEmails(emails);
			}
			if (!Persistence.getPersistenceUtil().isLoaded(newLead, "gruposUsuarios")) {
				List<GrupoUsuariosLead> grupoUsuarios = grupoUsuariosLeadListProducer
						.retrieveAllGrupoUsuariosLeadByLead(newLead);
				newLead.setGruposUsuarios(grupoUsuarios);
			}
			if (!Persistence.getPersistenceUtil().isLoaded(newLead, "atividadesAgenda")) {
				List<AtividadeAgenda> atividadesAgenda = atividadeAgendaListProducer
						.retrieveAllAtividadeAgendasByLeadOrderedByDataEHora(newLead);
				newLead.setAtividadesAgenda(atividadesAgenda);
			}
			if (!Persistence.getPersistenceUtil().isLoaded(newLead, "contatos")) {
				List<Contato> contatos = contatoListProducer.retrieveAllContatosByLeadOrderedByNome(newLead);
				newLead.setContatos(contatos);
			}
			if (!Persistence.getPersistenceUtil().isLoaded(newLead, "anexos")) {
				List<Anexo> anexos = anexoListProducer.retrieveAllAnexosByLeadOrderedByDescricao(newLead);
				newLead.setAnexos(anexos);
			}
			if(!Persistence.getPersistenceUtil().isLoaded(newLead, "notas")) {
				List<Nota> notas = notaListProducer.retrieveAllNotasByLeadOrderedByDescricao(newLead);
				newLead.setNotas(notas);
			}
			if(newLead.getUf() == null)
			{
				newLead.setUf(new UF());
			}
			if(newLead.getCidade() == null)
			{
				newLead.setCidade(new Cidade());
			}
			if(newLead.getCidadesPorUf() == null)
			{
				newLead.setCidadesPorUf(new ArrayList<Cidade>());
			}
			if(newLead.getOrigem() == null)
			{
				newLead.setOrigem(new Origem());
			}
			if(newLead.getAtividade() == null)
			{
				newLead.setAtividade(new Atividade());
			}
		}
	}
	@PostConstruct
	public void initNewLead() {
			newLead = new Lead();
			Principals principal = loginBean.getPrincipalsFromDB();
			principal.setGruposUsuarios(
					grupoUsuariosPrincipalsListProducer.retrieveAllGrupoUsuariosPrincipalsByPrincipal(principal));
			for (GrupoUsuariosPrincipals grupoUsuarios : principal.getGruposUsuarios()) {
				GrupoUsuariosLead grupoUsuariosLead = new GrupoUsuariosLead();
				grupoUsuariosLead.setGrupoUsuarios(grupoUsuarios.getGrupoUsuarios());
//				grupoUsuariosLead.setLead(newLead);
				newLead.getGruposUsuarios().add(grupoUsuariosLead);
			}
			newLead.setLeadPai(new Lead());
	}
	public StreamedContent carregaImagem(Lead lead) throws IOException
	{
		Arquivo arquivo = (Arquivo)em.createNamedQuery("Lead.findImagemById").setParameter("id", lead.getId()).getSingleResult();
		if(arquivo != null && arquivo.getContent() != null)
		{
	        StreamedContent streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(arquivo.getContent()),arquivo.getContentType());
	        return streamedContent;
		}
		else
		{
			return new DefaultStreamedContent();
		}
	}
}
