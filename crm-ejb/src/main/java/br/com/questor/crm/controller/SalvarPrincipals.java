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

import org.apache.commons.io.IOUtils;
import org.jboss.security.auth.spi.Util;

import br.com.questor.crm.data.GrupoUsuariosPrincipalsListProducer;
import br.com.questor.crm.data.RolesListProducer;
import br.com.questor.crm.model.GrupoUsuarios;
import br.com.questor.crm.model.GrupoUsuariosPrincipals;
import br.com.questor.crm.model.ImagePart;
import br.com.questor.crm.model.Imagem;
import br.com.questor.crm.model.Principals;
import br.com.questor.crm.model.Roles;

@Stateful
//@Model
@Named("salvarPrincipals")
@SessionScoped
public class SalvarPrincipals extends BaseController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9186625823996833337L;
	
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	private Principals newPrincipal;

	@Inject
	private Event<Principals> principalsEventSrc;
	
	@Inject
	private GrupoUsuariosPrincipalsListProducer grupoUsuariosPrincipalsListProducer;
	
	@Inject
	private RolesListProducer rolesListProducer;
	
	@Produces
	@Named
	public Principals getNewPrincipal() {
		return newPrincipal;
	}
	
	public String novo()
	{
		initNewPrincipal();
		return "/pages/protected/admin/principals?faces-redirect=true";
	}
	
	public void adicionarGrupoUsuarios(String id)
	{
		if(id != null && !"".equalsIgnoreCase(id))
		{
			GrupoUsuarios grupoUsuarios = em.find(GrupoUsuarios.class, Long.parseLong(id));
			GrupoUsuariosPrincipals grupoUsuariosPrincipals = new GrupoUsuariosPrincipals();
			grupoUsuariosPrincipals.setPrincipals(newPrincipal);
			grupoUsuariosPrincipals.setGrupoUsuarios(grupoUsuarios);
			newPrincipal.getGruposUsuarios().add(grupoUsuariosPrincipals);
		}
	}

	public void salvar() throws Exception {
		log.info("Salvando Principal" + newPrincipal.getPrincipalID());
		if(newPrincipal.getImagemPart() != null)
		{
			Imagem imagem = new Imagem();
			imagem.setNome(newPrincipal.getImagemPart().getName());
			imagem.setSize(newPrincipal.getImagemPart().getSize());
			imagem.setContentType(newPrincipal.getImagemPart().getContentType());
			imagem.setImagem(IOUtils.toByteArray(newPrincipal.getImagemPart().getInputStream()));
			newPrincipal.setImagem(imagem);
			em.persist(imagem);
		}
		if(newPrincipal.getAssinaturaPart() != null)
		{
			Imagem assinatura = new Imagem();
			assinatura.setNome(newPrincipal.getAssinaturaPart().getName());
			assinatura.setSize(newPrincipal.getAssinaturaPart().getSize());
			assinatura.setContentType(newPrincipal.getAssinaturaPart().getContentType());
			assinatura.setImagem(IOUtils.toByteArray(newPrincipal.getAssinaturaPart().getInputStream()));
			newPrincipal.setAssinaturaEmail(assinatura);
			em.persist(assinatura);
		}
		if(newPrincipal.getId() == null)
		{
			if(newPrincipal.getRole().getRole() == null)
			{
				Roles newRole = new Roles();
				newRole.setPrincipalID(newPrincipal.getPrincipalID());
				newRole.setRole("USER");
				newRole.setRoleGroup("USUARIOS");
				newPrincipal.setRole(newRole);
			}
			else
			{
				if("USER".equalsIgnoreCase(newPrincipal.getRole().getRole()))
				{
					Roles newRole = new Roles();
					newRole.setPrincipalID(newPrincipal.getPrincipalID());
					newRole.setRole("USER");
					newRole.setRoleGroup("USUARIOS");
					newPrincipal.setRole(newRole);
				}
				else
				{
					Roles newRole = new Roles();
					newRole.setPrincipalID(newPrincipal.getPrincipalID());
					newRole.setRole("ADMIN");
					newRole.setRoleGroup("ADMINISTRADORES");
					newPrincipal.setRole(newRole);
				}
			}
			newPrincipal.setPassword("t+lL5RPpboxFzSPRYideWhLr3pEApCXE683X+k3NiXw=");
			em.persist(newPrincipal.getRole());
			em.persist(newPrincipal);
			for(GrupoUsuariosPrincipals grupoUsuarios:newPrincipal.getGruposUsuarios())
			{
				grupoUsuarios.setPrincipals(newPrincipal);
				em.persist(grupoUsuarios);
			}
		}
		else
		{
			newPrincipal.setPrimeiroLogin(Boolean.FALSE);
			for(GrupoUsuariosPrincipals grupoUsuarios:newPrincipal.getGruposUsuarios())
			{
				if(grupoUsuarios.getId() == null)
				{
					grupoUsuarios.setPrincipals(newPrincipal);
					em.persist(grupoUsuarios);
				}
			}
			if(!"".equalsIgnoreCase(newPrincipal.getPassword()))
			{
				newPrincipal.setPassword(Util.createPasswordHash("SHA-256","BASE64",null, null, newPrincipal.getPassword()));
			}
			em.merge(newPrincipal);
		}
		principalsEventSrc.fire(newPrincipal);
		initNewPrincipal();
	}
	public String primeiroAcesso(Principals principals)
	{
		newPrincipal = principals;
		lazyInicializationPrincipals();
		return "/pages/protected/user/principals";
	}
	public String editar(Principals principals)
	{
		newPrincipal = principals;
		lazyInicializationPrincipals();
		return "/pages/protected/admin/principals";
	}
	private void lazyInicializationPrincipals()
	{
		if(newPrincipal != null && newPrincipal.getId() != null)
		{
			if(newPrincipal.getImagem() != null)
			{
				ImagePart imagemPart = new ImagePart();
				imagemPart.setSize(newPrincipal.getImagem().getSize());
				imagemPart.setName(newPrincipal.getImagem().getNome());
				imagemPart.setInputStream(IOUtils.toInputStream(new String(newPrincipal.getImagem().getImagem())));
				imagemPart.setContentType(newPrincipal.getImagem().getContentType());
				newPrincipal.setImagemPart(imagemPart);
			}
			List<GrupoUsuariosPrincipals> gruposUsuariosPrincipals = grupoUsuariosPrincipalsListProducer.retrieveAllGrupoUsuariosPrincipalsByPrincipal(newPrincipal);
			newPrincipal.setGruposUsuarios(gruposUsuariosPrincipals);
			Roles roles = rolesListProducer.retrieveAllRolesByPrincipalOrderedByNome(newPrincipal);
			newPrincipal.setRole(roles);
		}
	}
	@PostConstruct
	public void initNewPrincipal() {
		newPrincipal = new Principals();
	}
}
