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
import javax.persistence.PersistenceException;

import org.apache.commons.io.IOUtils;
import org.jboss.security.auth.spi.Util;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.questor.crm.data.GrupoUsuariosPrincipalsListProducer;
import br.com.questor.crm.data.RolesListProducer;
import br.com.questor.crm.model.Arquivo;
import br.com.questor.crm.model.Cargo;
import br.com.questor.crm.model.GrupoUsuarios;
import br.com.questor.crm.model.GrupoUsuariosPrincipals;
import br.com.questor.crm.model.ImagePart;
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
	
	private Lead distribuidor;
	
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
	public void gerarThumbnail(Arquivo imagem) throws Exception
	{
		BufferedImage bufferedImage = new BufferedImage(30,40,BufferedImage.TYPE_INT_RGB);
		ByteArrayInputStream bais = new ByteArrayInputStream(imagem.getContent());
		Image image = ImageIO.read(bais).getScaledInstance(30, 40, BufferedImage.SCALE_SMOOTH);
		bufferedImage.createGraphics().drawImage(image, 0, 0, null);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
		
		Arquivo thumbnail = null;
		try
		{
			thumbnail = (Arquivo) em.createNamedQuery("Principals.findThumbnailById").setParameter("id", newPrincipal.getId()).getSingleResult();
		}
		catch(Exception e)
		{
			log.severe("Não tem thumbnail");
		}
		if(thumbnail == null)
		{
			thumbnail = new Arquivo();
			thumbnail.setNome(newPrincipal.getImagemPart().getName());
			thumbnail.setSize(byteArrayOutputStream.toByteArray().length);
			thumbnail.setContentType(newPrincipal.getImagemPart().getContentType());
			thumbnail.setContent(byteArrayOutputStream.toByteArray());
			newPrincipal.setThumbnail(thumbnail);
			em.persist(thumbnail);
		}
		else
		{
			thumbnail.setNome(newPrincipal.getImagemPart().getName());
			thumbnail.setSize(byteArrayOutputStream.toByteArray().length);
			thumbnail.setContentType(newPrincipal.getImagemPart().getContentType());
			thumbnail.setContent(byteArrayOutputStream.toByteArray());
			em.merge(thumbnail);
		}
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
			if(!newPrincipal.getPassword().equals(newPrincipal.getConfirmarSenha()))
			{
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senhas não conferem.", null);
				FacesContext.getCurrentInstance().addMessage("regPrincipals:senha", m);
				FacesContext.getCurrentInstance().addMessage("regPrincipals:confirmarSenha", m);
			}
		}
		return Boolean.TRUE;
	}
	public void salvaImagemEAssinatura() throws Exception
	{
		Arquivo imagem = null;
		try
		{
			imagem = (Arquivo) em.createNamedQuery("Principals.findImagemById").setParameter("id", newPrincipal.getId()).getSingleResult();
		}
		catch(Exception e)
		{
			log.info("Imagem não encontrada");
		}
		if(newPrincipal.getImagemPart() != null)
		{
			if(imagem == null)
			{
				imagem = new Arquivo();
				imagem.setNome(newPrincipal.getImagemPart().getName());
				imagem.setSize(newPrincipal.getImagemPart().getSize());
				imagem.setContentType(newPrincipal.getImagemPart().getContentType());
				imagem.setContent(IOUtils.toByteArray(newPrincipal.getImagemPart().getInputStream()));
				em.persist(imagem);
				newPrincipal.setImagem(imagem);
				gerarThumbnail(imagem);
			}
			else
			{
				imagem.setNome(newPrincipal.getImagemPart().getName());
				imagem.setSize(newPrincipal.getImagemPart().getSize());
				imagem.setContentType(newPrincipal.getImagemPart().getContentType());
				imagem.setContent(IOUtils.toByteArray(newPrincipal.getImagemPart().getInputStream()));
				em.merge(imagem);
				try
				{
					gerarThumbnail(imagem);
				}
				catch(Exception e)
				{
					log.severe("Falha ao gerar Thumbnail");
				}
			}
			
		}
		else
		{
			if(imagem == null)
			{
				imagem = new Arquivo();
				imagem.setNome("Imagem Padrão");
				imagem.setSize(120);
				imagem.setContentType("image/jpeg");
				imagem.setContent(gerarImagemPadrao());
				em.persist(imagem);
				newPrincipal.setImagem(imagem);
				newPrincipal.setThumbnail(imagem);
			}
		}
		Arquivo assinatura = null;
		try
		{
			assinatura = (Arquivo) em.createNamedQuery("Principals.findAssinaturaById").setParameter("id", newPrincipal.getId()).getSingleResult();
		}
		catch(Exception e)
		{
			log.info("Imagem não encontrada");
		}
		if(newPrincipal.getAssinaturaPart() != null)
		{
			if(assinatura == null)
			{
				assinatura = new Arquivo();
				assinatura.setNome(newPrincipal.getAssinaturaPart().getName());
				assinatura.setSize(newPrincipal.getAssinaturaPart().getSize());
				assinatura.setContentType(newPrincipal.getAssinaturaPart().getContentType());
				assinatura.setContent(IOUtils.toByteArray(newPrincipal.getAssinaturaPart().getInputStream()));
				em.persist(assinatura);
				newPrincipal.setAssinaturaEmail(assinatura);
			}
			else
			{
				assinatura.setNome(newPrincipal.getAssinaturaPart().getName());
				assinatura.setSize(newPrincipal.getAssinaturaPart().getSize());
				assinatura.setContentType(newPrincipal.getAssinaturaPart().getContentType());
				assinatura.setContent(IOUtils.toByteArray(newPrincipal.getAssinaturaPart().getInputStream()));
				em.merge(assinatura);
			}
		}
		else
		{
			if(assinatura == null)
			{
				newPrincipal.setAssinaturaEmail(null);
			}
		}
	}
	private void salvarNovoUsuario()
	{
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
			em.persist(newPrincipal);
			em.persist(newPrincipal.getRole());
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
				if(distribuidor.getId() == null)
				{
					newPrincipal.setDistribuidor(null);
				}
				else
				{
					newPrincipal.setDistribuidor(distribuidor);
				}
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
		try
		{
			return newPrincipal != null && newPrincipal.getImagem() != null && newPrincipal.getImagem().getId() != null;
		}
		catch(Exception e)
		{
			log.info("Imagem nao carregada");
			return Boolean.FALSE;
		}
	}
	public boolean assinaturaCarregada()
	{
		try
		{
			return newPrincipal != null && newPrincipal.getAssinaturaEmail() != null && newPrincipal.getAssinaturaEmail().getId() != null;
		}
		catch(Exception e)
		{
			log.info("Assinatura nao carregada");
			return Boolean.FALSE;
		}
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
			if(newPrincipal.getImagem() != null)
			{
				try
				{
					Arquivo imagem = (Arquivo)em.createNamedQuery("Principals.findImagemById").setParameter("id", newPrincipal.getId()).getSingleResult();
					newPrincipal.setImagem(imagem);
					ImagePart imagemPart = new ImagePart();
					imagemPart.setSize(newPrincipal.getImagem().getSize());
					imagemPart.setName(newPrincipal.getImagem().getNome());
					imagemPart.setInputStream(IOUtils.toInputStream(new String(newPrincipal.getImagem().getContent())));
					imagemPart.setContentType(newPrincipal.getImagem().getContentType());
					newPrincipal.setImagemPart(imagemPart);
				}
				catch(Exception e)
				{
					log.info("Sem imagem salva");
				}
			}
			if(newPrincipal.getAssinaturaEmail() != null)
			{
				try
				{
					Arquivo assinatura = (Arquivo)em.createNamedQuery("Principals.findAssinaturaById").setParameter("id", newPrincipal.getId()).getSingleResult();
					newPrincipal.setAssinaturaEmail(assinatura);
					ImagePart imagemPart = new ImagePart();
					imagemPart.setSize(newPrincipal.getAssinaturaEmail().getSize());
					imagemPart.setName(newPrincipal.getAssinaturaEmail().getNome());
					imagemPart.setInputStream(IOUtils.toInputStream(new String(newPrincipal.getAssinaturaEmail().getContent())));
					imagemPart.setContentType(newPrincipal.getAssinaturaEmail().getContentType());
					newPrincipal.setAssinaturaPart(imagemPart);
				}
				catch(Exception e)
				{
					log.info("Sem assinatura salva");
				}
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
				this.distribuidor = distribuidor;
			}
			catch(Exception nre)
			{
				log.log(Level.SEVERE, "Não tem distribuidor");
				newPrincipal.setDistribuidor(null);
				this.distribuidor = new Lead();
			}
		}
	}
	@PostConstruct
	public void initNewPrincipal() {
		newPrincipal = new Principals();
		this.distribuidor = new Lead();
//		newPrincipal.setDistribuidor(new Lead());
	}
	public StreamedContent carregaAssinatura(Principals principals) throws IOException
	{
		Arquivo imagem = null;
		try
		{
			imagem = (Arquivo)em.createNamedQuery("Principals.findAssinaturaById").setParameter("id", principals.getId()).getSingleResult();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "Não há assinatura cadastrada");
		}
		return carregaImagem(imagem);
	}
	public StreamedContent carregaImagem(Principals principals) throws IOException
	{
		Arquivo imagem = null;
		try
		{
			imagem = (Arquivo)em.createNamedQuery("Principals.findImagemById").setParameter("id", principals.getId()).getSingleResult();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "Não há imagem cadastrada");
		}
		return carregaImagem(imagem);
	}
	public StreamedContent carregaImagem(Arquivo imagem)
	{
		if(imagem != null && imagem.getContent() != null)
		{
	        StreamedContent streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(imagem.getContent()),imagem.getContentType());
	        return streamedContent;
		}
		else
		{
			return new DefaultStreamedContent();
		}
	}
	public Lead getDistribuidor() {
		return distribuidor;
	}
	public void setDistribuidor(Lead distribuidor) {
		this.distribuidor = distribuidor;
	}
}
