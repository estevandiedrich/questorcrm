package br.com.questor.crm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
		this.dataCadastro = new Date();
		this.gruposUsuarios = new ArrayList<GrupoUsuariosLead>();
		this.grupoUsuariosSelecionado = new GrupoUsuarios();
		this.newAtividadeAgenda = new AtividadeAgenda();
		this.atividadesAgenda = new ArrayList<AtividadeAgenda>();
		this.emails = new ArrayList<Email>();
		this.contatos = new ArrayList<Contato>();
		this.anexos = new ArrayList<Anexo>();
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
	
	@Transient
	private AtividadeAgenda newAtividadeAgenda;

	@OneToMany(mappedBy = "lead", targetEntity = AtividadeAgenda.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<AtividadeAgenda> atividadesAgenda;
	
	private Date dataCadastro;
	
	@ManyToOne
	  @JoinColumn(name = "leadpai_id",nullable = true)
	private Lead leadPai;
	
//	@ManyToMany
//    @JoinTable(name="grupousuarios_lead", joinColumns={@JoinColumn(name="lead_id")}, inverseJoinColumns={@JoinColumn(name="grupousuarios_id")})
	@OneToMany(mappedBy = "lead", targetEntity = GrupoUsuariosLead.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<GrupoUsuariosLead> gruposUsuarios;
	
	@OneToMany(mappedBy = "lead", targetEntity = Email.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<Email> emails; 
	
	@ManyToOne
	  @JoinColumn(name = "imagem_id")
	private Imagem imagem;
	
	@Transient
	private Part imagemPart;
	
	@OneToMany(mappedBy = "lead", targetEntity = Anexo.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<Anexo> anexos;
	
	@ManyToOne
	  @JoinColumn(name = "principals_id")
	private Principals usuarioQueCadastrou;

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

	public List<GrupoUsuariosLead> getGruposUsuarios() {
		return gruposUsuarios;
	}

	public void setGruposUsuarios(List<GrupoUsuariosLead> grupoUsuarios) {
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

	public Principals getUsuarioQueCadastrou() {
		return usuarioQueCadastrou;
	}

	public void setUsuarioQueCadastrou(Principals usuarioQueCadastrou) {
		this.usuarioQueCadastrou = usuarioQueCadastrou;
	}

	public AtividadeAgenda getNewAtividadeAgenda() {
		return newAtividadeAgenda;
	}

	public void setNewAtividadeAgenda(AtividadeAgenda newAtividadeAgenda) {
		this.newAtividadeAgenda = newAtividadeAgenda;
	}

	public List<AtividadeAgenda> getAtividadesAgenda() {
		return atividadesAgenda;
	}

	public void setAtividadesAgenda(List<AtividadeAgenda> atividadesAgenda) {
		this.atividadesAgenda = atividadesAgenda;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
}
