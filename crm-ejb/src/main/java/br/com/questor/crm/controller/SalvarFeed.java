package br.com.questor.crm.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.questor.crm.model.Feed;
import br.com.questor.crm.model.Imagem;

@Stateful
@Model
public class SalvarFeed {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private LoginBean loginBean;

	@Inject
	private Event<Feed> feedEventSrc;

	private Feed newFeed;

	@Produces
	@Named
	public Feed getNewFeed() {
		return newFeed;
	}

	public boolean imagemCarregada() {
		return newFeed != null && newFeed.getUsuarioQueCriou() != null
				&& newFeed.getUsuarioQueCriou().getImagem() != null;
	}

	public StreamedContent carregaImagem() throws IOException {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
	        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
	        String feedId = (String) myRequest.getParameter("_feed");
	        if(feedId != null && !"".equalsIgnoreCase(feedId))
	        {
				Feed feed = (Feed) em.createNamedQuery("Feed.findById").setParameter("id", Long.parseLong(feedId)).getSingleResult();
				if (feed != null) {
					Imagem imagem = (Imagem) em.createNamedQuery("Principals.findImagemById")
							.setParameter("id", feed.getUsuarioQueCriou().getId()).getSingleResult();
					if (imagem != null && imagem.getImagem() != null) {
						StreamedContent streamedContent = new DefaultStreamedContent(
								new ByteArrayInputStream(imagem.getImagem()), imagem.getContentType());
						return streamedContent;
					} else {
						
			            return gerarImagemPadrao();
					}
				} else {
					return gerarImagemPadrao();
				}
			} else {
				return gerarImagemPadrao();
			}
		} catch (NoResultException e) {
			return gerarImagemPadrao();
		}
	}
	public DefaultStreamedContent gerarImagemPadrao()
	{
		BufferedImage bufferedImg = new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImg.createGraphics();
        g2.drawString("Imagem nao encontrada", 0, 10);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImg, "png", os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "image/png");
	}
	public void salvar() throws Exception {
		log.info("Salvando Feed " + newFeed.getId());
		newFeed.setUsuarioQueCriou(loginBean.getPrincipalsFromDB());
		em.persist(newFeed);
		feedEventSrc.fire(newFeed);
		initNewFeed();
	}

	public void excluir(Feed feed) {
		log.info("Excluindo Feed " + feed.getId());
		em.remove(em.contains(feed) ? feed : em.merge(feed));
		feedEventSrc.fire(feed);
		initNewFeed();
	}

	@PostConstruct
	public void initNewFeed() {
		newFeed = new Feed();
	}
}
