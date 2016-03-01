package br.com.questor.crm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="LEAD_SEQUENCE", sequenceName="LEAD_SEQUENCE", allocationSize=1, initialValue=1)
public class Lead implements Serializable {
	public Lead()
	{
		this.leadPai = null;
		this.gruposUsuarios = new ArrayList<GrupoUsuarios>();
		this.grupoUsuariosSelecionado = new GrupoUsuarios();
		this.emails = new ArrayList<Email>();
		this.contatos = new ArrayList<Contato>();
		this.contatoSelecionado = new Contato();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -2921454420621654968L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="LEAD_SEQUENCE")
	private Long id;
	
	@NotNull
	private String nome;
	
	@OneToMany(mappedBy = "lead", targetEntity = Contato.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<Contato> contatos;
	
	@Transient
	private Contato contatoSelecionado;
	
	@Transient
	private GrupoUsuarios grupoUsuariosSelecionado;
	
	@ManyToOne
	  @JoinColumn(name = "leadpai_id",nullable = true)
	private Lead leadPai;
	
	@OneToMany(mappedBy = "lead", targetEntity = GrupoUsuarios.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<GrupoUsuarios> gruposUsuarios;
	
	@OneToMany(mappedBy = "lead", targetEntity = Proposta.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<Email> emails; 
	
	@ManyToOne
	  @JoinColumn(name = "imagem_id")
	private Imagem imagem;
	
	@Transient
	private Part imagemPart;
	
	@OneToMany(mappedBy = "lead", targetEntity = Anexo.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<Anexo> anexos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Email> getEmails() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	public Imagem getImagem() {
		return imagem;
	}

	public void setImagem(Imagem imagem) {
		this.imagem = imagem;
	}

	public Part getImagemPart() {
		return imagemPart;
	}

	public void setImagemPart(Part imagemPart) {
		this.imagemPart = imagemPart;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	public List<GrupoUsuarios> getGruposUsuarios() {
		return gruposUsuarios;
	}

	public void setGruposUsuarios(List<GrupoUsuarios> grupoUsuarios) {
		this.gruposUsuarios = grupoUsuarios;
	}

	public List<Anexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<Anexo> anexos) {
		this.anexos = anexos;
	}

	public Lead getLeadPai() {
		return leadPai;
	}

	public void setLeadPai(Lead leadPai) {
		this.leadPai = leadPai;
	}

	public Contato getContatoSelecionado() {
		return contatoSelecionado;
	}

	public void setContatoSelecionado(Contato contatoSelecionado) {
		this.contatoSelecionado = contatoSelecionado;
	}

	public GrupoUsuarios getGrupoUsuariosSelecionado() {
		return grupoUsuariosSelecionado;
	}

	public void setGrupoUsuariosSelecionado(GrupoUsuarios grupoUsuariosSelecionado) {
		this.grupoUsuariosSelecionado = grupoUsuariosSelecionado;
	}		
}
