package br.com.questor.crm.controller;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.io.IOUtils;
import org.jboss.security.auth.spi.Util;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.questor.crm.data.GrupoUsuariosPrincipalsListProducer;
import br.com.questor.crm.data.RolesListProducer;
import br.com.questor.crm.model.Cargo;
import br.com.questor.crm.model.GrupoUsuarios;
import br.com.questor.crm.model.GrupoUsuariosPrincipals;
import br.com.questor.crm.model.ImagePart;
import br.com.questor.crm.model.Imagem;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.Principals;
import br.com.questor.crm.model.Roles;

@Stateful
//@Model
@Named("salvarPrincipals")
@SessionScoped
public class SalvarPrincipals implements Serializable {
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
	
	@Inject
	private SalvarEmail salvarEmail;
	
	@Inject
	private LoginBean loginBean;
	
	@Produces
	@Named
	public Principals getNewPrincipal() {
		return newPrincipal;
	}
	public String listagem()
	{
		return "/pages/protected/admin/listagemprincipals?faces-redirect=true";
	}
	public String novo()
	{
		initNewPrincipal();
		return "/pages/protected/admin/principals?faces-redirect=true";
	}
	public void removerGrupoUsuarios(GrupoUsuariosPrincipals grupoUsuarios)
	{
		GrupoUsuariosPrincipals remover = null;
		for(GrupoUsuariosPrincipals grupoUsuariosPrincipals:newPrincipal.getGruposUsuarios())
		{
			if(grupoUsuariosPrincipals.getGrupoUsuarios().getId() == grupoUsuarios.getGrupoUsuarios().getId())
			{
				remover = grupoUsuariosPrincipals;
				break;
			}
		}
		newPrincipal.getGruposUsuarios().remove(remover);
		if(remover.getId() != null)
		{
			em.remove(em.contains(remover) ? remover:em.merge(remover));
		}
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
			if(newPrincipal.getId() != null)
			{
				for(GrupoUsuariosPrincipals gu:newPrincipal.getGruposUsuarios())
				{
					gu.setPrincipals(newPrincipal);
					em.persist(gu);
				}
			}
		}
	}
	public byte[] gerarImagemPadrao()
	{
		BufferedImage bufferedImg = new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImg.createGraphics();
        g2.drawString("Imagem nao encontrada", 0, 10);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImg, "jpg", os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return os.toByteArray();
	}
	public void excluir(Principals principal)
	{
		log.info("Excluindo Principal " + principal.getPrincipalId());
		em.remove(em.contains(principal) ? principal:em.merge(principal));
		principalsEventSrc.fire(principal);
		initNewPrincipal();
	}
	public void gerarThumbnail(Imagem imagem) throws Exception
	{
		BufferedImage bufferedImage = new BufferedImage(30,40,BufferedImage.TYPE_INT_RGB);
		ByteArrayInputStream bais = new ByteArrayInputStream(imagem.getImagem());
		Image image = ImageIO.read(bais).getScaledInstance(30, 40, BufferedImage.SCALE_SMOOTH);
		bufferedImage.createGraphics().drawImage(image, 0, 0, null);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
		Imagem thumbnail = new Imagem();
		thumbnail.setNome(newPrincipal.getImagemPart().getName());
		thumbnail.setSize(120);
		thumbnail.setContentType(newPrincipal.getImagemPart().getContentType());
		thumbnail.setImagem(byteArrayOutputStream.toByteArray());
		newPrincipal.setThumbnail(thumbnail);
		em.persist(thumbnail);
	}
	public boolean valido()
	{
		if(newPrincipal.getCargo().getId() == null)
		{
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não pode estar vazio.", null);
            FacesContext.getCurrentInstance().addMessage("regPrincipals:cargo", m);
            return Boolean.FALSE;
		}
		if(newPrincipal.getGruposUsuarios().isEmpty())
		{
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não pode estar vazio.", null);
			FacesContext.getCurrentInstance().addMessage("regPrincipals:grupo", m);
			return Boolean.FALSE;
		}
		if(!loginBean.isCallerInRole("ADMIN"))
		{
			if("".equalsIgnoreCase(newPrincipal.getPassword()))
			{
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não pode estar vazio.", null);
				FacesContext.getCurrentInstance().addMessage("regPrincipals:senha", m);
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}
	public void salvaImagemEAssinatura() throws Exception
	{
		if(newPrincipal.getImagemPart() != null)
		{
			if(newPrincipal.getImagem() == null || newPrincipal.getImagem().getId() == null)
			{
				Imagem imagem = new Imagem();
				imagem.setNome(newPrincipal.getImagemPart().getName());
				imagem.setSize(newPrincipal.getImagemPart().getSize());
				imagem.setContentType(newPrincipal.getImagemPart().getContentType());
				imagem.setImagem(IOUtils.toByteArray(newPrincipal.getImagemPart().getInputStream()));
				newPrincipal.setImagem(imagem);
				em.persist(imagem);
				gerarThumbnail(imagem);
			}
			else
			{
				Imagem imagem = newPrincipal.getImagem();
				imagem.setNome(newPrincipal.getImagemPart().getName());
				imagem.setSize(newPrincipal.getImagemPart().getSize());
				imagem.setContentType(newPrincipal.getImagemPart().getContentType());
				imagem.setImagem(IOUtils.toByteArray(newPrincipal.getImagemPart().getInputStream()));
				em.merge(imagem);
				gerarThumbnail(imagem);
			}
			
		}
		else
		{
			if(newPrincipal.getImagem() == null || newPrincipal.getImagem().getId() == null)
			{
				Imagem imagem = new Imagem();
				imagem.setNome("Imagem Padrão");
				imagem.setSize(120);
				imagem.setContentType("image/jpeg");
				imagem.setImagem(gerarImagemPadrao());
				newPrincipal.setImagem(imagem);
				newPrincipal.setThumbnail(imagem);
				em.persist(imagem);
			}
		}
		if(newPrincipal.getAssinaturaPart() != null)
		{
			if(newPrincipal.getAssinaturaEmail() == null || newPrincipal.getAssinaturaEmail().getId() == null)
			{
				Imagem assinatura = new Imagem();
				assinatura.setNome(newPrincipal.getAssinaturaPart().getName());
				assinatura.setSize(newPrincipal.getAssinaturaPart().getSize());
				assinatura.setContentType(newPrincipal.getAssinaturaPart().getContentType());
				assinatura.setImagem(IOUtils.toByteArray(newPrincipal.getAssinaturaPart().getInputStream()));
				newPrincipal.setAssinaturaEmail(assinatura);
				em.persist(assinatura);
			}
			else
			{
				Imagem assinatura = newPrincipal.getAssinaturaEmail();
				assinatura.setNome(newPrincipal.getAssinaturaPart().getName());
				assinatura.setSize(newPrincipal.getAssinaturaPart().getSize());
				assinatura.setContentType(newPrincipal.getAssinaturaPart().getContentType());
				assinatura.setImagem(IOUtils.toByteArray(newPrincipal.getAssinaturaPart().getInputStream()));
				em.merge(assinatura);
			}
		}
		else
		{
			newPrincipal.setAssinaturaEmail(null);
		}
	}
	private void salvarNovoUsuario()
	{
		if(newPrincipal.getDistribuidor().getId() == null)
		{
			newPrincipal.setDistribuidor(null);
		}
		if(newPrincipal.getRole().getRole() == null)
		{
			Roles newRole = new Roles();
			newRole.setPrincipalID(newPrincipal.getPrincipalId());
			newRole.setRole("USER");
			newRole.setRoleGroup("USUARIOS");
			newPrincipal.setRole(newRole);
		}
		else
		{
			if("USER".equalsIgnoreCase(newPrincipal.getRole().getRole()))
			{
				Roles newRole = new Roles();
				newRole.setPrincipalID(newPrincipal.getPrincipalId());
				newRole.setRole("USER");
				newRole.setRoleGroup("USUARIOS");
				newPrincipal.setRole(newRole);
			}
			else
			{
				Roles newRole = new Roles();
				newRole.setPrincipalID(newPrincipal.getPrincipalId());
				newRole.setRole("ADMIN");
				newRole.setRoleGroup("ADMINISTRADORES");
				newPrincipal.setRole(newRole);
			}
		}
		if(newPrincipal.getGruposUsuarios().isEmpty())
		{
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não pode estar vazio.", null);
			FacesContext.getCurrentInstance().addMessage("regPrincipals:grupo", m);
		}
		else
		{
			int[] senha = new int[6];
			String senhaNaoCifrada = "";
			Random random = new Random();
			for(int i = 0;i < 6;i++)
			{
				senha[i] = random.nextInt(9);
			}
			senhaNaoCifrada = Arrays.toString(senha).replace("[", "").replace(",", "").replace(" ","").replace("]", "");
			newPrincipal.setPassword(Util.createPasswordHash("SHA-256","BASE64",null, null, senhaNaoCifrada));
			em.persist(newPrincipal.getRole());
			em.persist(newPrincipal);
			salvarEmail.enviarEmailNovoUsuario(newPrincipal,senhaNaoCifrada);
//				Feed newFeed = new Feed();
//				newFeed.setTexto("Novo usuário cadastrado: "+newPrincipal.getNome());
//				newFeed.setUsuarioQueCriou(loginBean.getPrincipalsFromDB());
//				em.persist(newFeed);
			for(GrupoUsuariosPrincipals grupoUsuarios:newPrincipal.getGruposUsuarios())
			{
				grupoUsuarios.setPrincipals(newPrincipal);
				em.persist(grupoUsuarios);
			}
			principalsEventSrc.fire(newPrincipal);
			initNewPrincipal();
		}
	}
	private void atualizarUsuario()
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
		if(!loginBean.isCallerInRole("ADMIN"))
		{
			if(!"".equalsIgnoreCase(newPrincipal.getPassword()))
			{
				newPrincipal.setPassword(Util.createPasswordHash("SHA-256","BASE64",null, null, newPrincipal.getPassword()));
				em.merge(newPrincipal);
			
				principalsEventSrc.fire(newPrincipal);
				initNewPrincipal();
			}
		}
		else
		{
			em.merge(newPrincipal);
			
			principalsEventSrc.fire(newPrincipal);
			initNewPrincipal();
			
		}
	}
	public void salvar() throws Exception {
		log.info("Salvando Principal" + newPrincipal.getPrincipalId());
		try
		{
			if(valido())
			{
				salvaImagemEAssinatura();
				if(newPrincipal.getId() == null)
				{
					salvarNovoUsuario();
				}
				else
				{
					atualizarUsuario();
				}
			}
		}
		catch(PersistenceException e)
		{
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getCause().getMessage(), null);
            FacesContext.getCurrentInstance().addMessage("erros", m);
		}
	}
	public boolean imagemCarregada()
	{
		return newPrincipal != null && newPrincipal.getImagem() != null && newPrincipal.getImagem().getId() != null;
	}
	public boolean assinaturaCarregada()
	{
		return newPrincipal != null && newPrincipal.getAssinaturaEmail() != null && newPrincipal.getAssinaturaEmail().getId() != null;
	}
	public String primeiroAcesso(Principals principals)
	{
		newPrincipal = principals;
		lazyInicializationPrincipals();
		return "/pages/protected/user/principals?faces-redirect=true";
	}
	public String editarMeuPerfil()
	{
		newPrincipal = loginBean.getPrincipalsFromDB();
		lazyInicializationPrincipals();
		return "/pages/protected/user/principals?faces-redirect=true";
	}
	public String editar(Principals principals)
	{
		newPrincipal = principals;
		lazyInicializationPrincipals();
		return "/pages/protected/admin/principals?faces-redirect=true";
	}
	private void lazyInicializationPrincipals()
	{		
		if(newPrincipal != null && newPrincipal.getId() != null)
		{
			if(newPrincipal.getDistribuidor() == null)
			{
				newPrincipal.setDistribuidor(new Lead());
			}
			if(newPrincipal.getImagem() != null)
			{
				Imagem imagem = (Imagem)em.createNamedQuery("Principals.findImagemById").setParameter("id", newPrincipal.getId()).getSingleResult();
				newPrincipal.setImagem(imagem);
				ImagePart imagemPart = new ImagePart();
				imagemPart.setSize(newPrincipal.getImagem().getSize());
				imagemPart.setName(newPrincipal.getImagem().getNome());
				imagemPart.setInputStream(IOUtils.toInputStream(new String(newPrincipal.getImagem().getImagem())));
				imagemPart.setContentType(newPrincipal.getImagem().getContentType());
				newPrincipal.setImagemPart(imagemPart);
			}
			if(newPrincipal.getAssinaturaEmail() != null)
			{
				Imagem assinatura = (Imagem)em.createNamedQuery("Principals.findAssinaturaById").setParameter("id", newPrincipal.getId()).getSingleResult();
				newPrincipal.setAssinaturaEmail(assinatura);
				ImagePart imagemPart = new ImagePart();
				imagemPart.setSize(newPrincipal.getAssinaturaEmail().getSize());
				imagemPart.setName(newPrincipal.getAssinaturaEmail().getNome());
				imagemPart.setInputStream(IOUtils.toInputStream(new String(newPrincipal.getAssinaturaEmail().getImagem())));
				imagemPart.setContentType(newPrincipal.getAssinaturaEmail().getContentType());
				newPrincipal.setAssinaturaPart(imagemPart);
			}
			List<GrupoUsuariosPrincipals> gruposUsuariosPrincipals = grupoUsuariosPrincipalsListProducer.retrieveAllGrupoUsuariosPrincipalsByPrincipal(newPrincipal);
			newPrincipal.setGruposUsuarios(gruposUsuariosPrincipals);
			Roles roles = rolesListProducer.retrieveAllRolesByPrincipalOrderedByNome(newPrincipal);
			newPrincipal.setRole(roles);
			Cargo cargo = (Cargo) em.createNamedQuery("Principals.findCargoById").setParameter("id", newPrincipal.getId()).getSingleResult();
			newPrincipal.setCargo(cargo);
			try
			{
				Lead distribuidor = (Lead) em.createNamedQuery("Principals.findDistribuidorById").setParameter("id", newPrincipal.getId()).getSingleResult();
				newPrincipal.setDistribuidor(distribuidor);
			}
			catch(NoResultException nre)
			{
				log.log(Level.SEVERE, "Não tem distribuidor", nre);
			}
		}
	}
	@PostConstruct
	public void initNewPrincipal() {
		newPrincipal = new Principals();
		newPrincipal.setDistribuidor(new Lead());
	}
	public StreamedContent carregaAssinatura(Principals principals) throws IOException
	{
		Imagem imagem = null;
		try
		{
			imagem = (Imagem)em.createNamedQuery("Principals.findAssinaturaById").setParameter("id", principals.getId()).getSingleResult();
		}
		catch(NoResultException e)
		{
			log.log(Level.SEVERE, "Não há assinatura cadastrada", e);
		}
		return carregaImagem(imagem);
	}
	public StreamedContent carregaImagem(Principals principals) throws IOException
	{
		Imagem imagem = null;
		try
		{
			imagem = (Imagem)em.createNamedQuery("Principals.findImagemById").setParameter("id", principals.getId()).getSingleResult();
		}
		catch(NoResultException e)
		{
			log.log(Level.SEVERE, "Não há imagem cadastrada");
		}
		return carregaImagem(imagem);
	}
	public StreamedContent carregaImagem(Imagem imagem)
	{
		if(imagem != null && imagem.getImagem() != null)
		{
	        StreamedContent streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(imagem.getImagem()),imagem.getContentType());
	        return streamedContent;
		}
		else
		{
			return new DefaultStreamedContent();
		}
	}
}
