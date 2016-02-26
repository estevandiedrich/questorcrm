package br.com.questor.crm.controller;

import java.io.Serializable;
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
//	@ManagedProperty("#{param.id}")
	private String id;

	@Inject
	private EntityManager em;

	private Principals newPrincipal;

	@Inject
	private Event<Principals> principalsEventSrc;
	

	@Produces
	@Named
	public Principals getNewPrincipal() {
		return newPrincipal;
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
			em.persist(newPrincipal);
		}
		else
		{
			newPrincipal.setPrimeiroLogin(Boolean.FALSE);
			newPrincipal.setPassword(Util.createPasswordHash("SHA-256","BASE64",null, null, newPrincipal.getPassword()));  

			em.merge(newPrincipal);
		}
		principalsEventSrc.fire(newPrincipal);
		initNewPrincipal();
	}

	public String editar(String id)
	{
		this.id = id;
		initNewPrincipal();
		return "/pages/protected/user/principalsedit";
	}

	@PostConstruct
	public void initNewPrincipal() {
//		this.id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if(this.id != null && this.id != "")
		{
			newPrincipal = em.find(Principals.class, Long.parseLong(this.id));
			ImagePart imagemPart = new ImagePart();
			imagemPart.setSize(newPrincipal.getImagem().getSize());
			imagemPart.setName(newPrincipal.getImagem().getNome());
			imagemPart.setInputStream(IOUtils.toInputStream(new String(newPrincipal.getImagem().getImagem())));
			imagemPart.setContentType(newPrincipal.getImagem().getContentType());
			newPrincipal.setImagemPart(imagemPart);
		}
		else
		{
			newPrincipal = new Principals();
		}
	}
}
