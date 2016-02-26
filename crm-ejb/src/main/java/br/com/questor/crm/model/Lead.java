package br.com.questor.crm.model;

import java.io.Serializable;
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
		this.grupo = new LeadGroup();
		this.grupoUsuarios = new GrupoUsuarios();
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
	
	@NotNull
	private String telefone;
	
	@NotNull
	private String email;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
	  @JoinColumn(name = "leadgroup_id")
	private LeadGroup grupo;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
	  @JoinColumn(name = "grupousuarios_id")
	private GrupoUsuarios grupoUsuarios;
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
	@JoinColumn(name="lead_id")
	private List<Email> emails; 
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	  @JoinColumn(name = "imagem_id")
	private Imagem imagem;
	
	@Transient
	private Part imagemPart;

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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LeadGroup getGrupo() {
		return grupo;
	}

	public void setGrupo(LeadGroup grupo) {
		this.grupo = grupo;
	}

	public GrupoUsuarios getGrupoUsuarios() {
		return grupoUsuarios;
	}

	public void setGrupoUsuarios(GrupoUsuarios grupoUsuarios) {
		this.grupoUsuarios = grupoUsuarios;
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
}
