package br.com.questor.crm.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.commons.io.IOUtils;

import br.com.questor.crm.model.Anexo;
import br.com.questor.crm.model.Email;
import br.com.questor.crm.model.Imagem;
import br.com.questor.crm.model.Lead;

@Stateful
@Model
public class SalvarAnexo {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Anexo> anexoEventSrc;
	
	private Anexo newAnexo;
	
	public void adicionar(Lead lead) throws IOException
	{
		Imagem imagem = new Imagem();
		imagem.setContentType(newAnexo.getPart().getContentType());
		imagem.setImagem(IOUtils.toByteArray(newAnexo.getPart().getInputStream()));
		imagem.setNome(newAnexo.getPart().getSubmittedFileName());
		imagem.setSize(newAnexo.getPart().getSize());
		newAnexo.setImagem(imagem);
		lead.getAnexos().add(newAnexo);
		initNewAnexo();
	}
	
	public void adicionar(Email email) throws IOException
	{
		Imagem imagem = new Imagem();
		imagem.setContentType(newAnexo.getPart().getContentType());
		imagem.setImagem(IOUtils.toByteArray(newAnexo.getPart().getInputStream()));
		imagem.setNome(newAnexo.getPart().getSubmittedFileName());
		imagem.setSize(newAnexo.getPart().getSize());
		newAnexo.setImagem(imagem);
		email.getAnexos().add(newAnexo);
		initNewAnexo();
	}
	
	@Produces
	@Named
	public Anexo getNewAnexo() {
		return newAnexo;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando Anexo" + newAnexo.getDescricao());
		em.persist(newAnexo);
		anexoEventSrc.fire(newAnexo);
		initNewAnexo();
	}

	@PostConstruct
	public void initNewAnexo() {
		newAnexo = new Anexo();
	}
}
