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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "principals",indexes = {
		@Index(columnList = "id", name = "principals_id_idx"),
		@Index(columnList = "role_id", name = "principals_role_id_idx"),
		@Index(columnList = "principalid", name = "principalid_idx")
		},
	uniqueConstraints = @UniqueConstraint(columnNames = "principalid")
)
@XmlRootElement
@NamedQueries(value={
		@NamedQuery(name = "Principals.findAll",query = "SELECT p FROM Principals p JOIN FETCH p.Role ORDER BY p.nome"),
		@NamedQuery(name = "Principals.findById",query = "SELECT p FROM Principals p JOIN FETCH p.Role WHERE p.id = :id"),
		@NamedQuery(name = "Principals.findByEmail",query = "SELECT p FROM Principals p JOIN FETCH p.Role WHERE p.principalId = :email"),
		@NamedQuery(name = "Principals.findImagemById",query = "SELECT p.imagem FROM Principals p WHERE p.id = :id"),
		@NamedQuery(name = "Principals.findThumbnailById",query = "SELECT p.thumbnail FROM Principals p WHERE p.id = :id"),
		@NamedQuery(name = "Principals.findAssinaturaById",query = "SELECT p.assinaturaEmail FROM Principals p WHERE p.id = :id"),
		@NamedQuery(name = "Principals.findCargoById",query = "SELECT p.cargo FROM Principals p WHERE p.id = :id"),
		@NamedQuery(name = "Principals.findDistribuidorById",query = "SELECT p.distribuidor FROM Principals p WHERE p.id = :id")
		}
)
@SequenceGenerator(name="PRINCIPALS_SEQUENCE", sequenceName="PRINCIPALS_SEQUENCE", allocationSize=1, initialValue=1)
public class Principals implements Serializable {
   /**
	 * 
	 */
	private static final long serialVersionUID = 6989116605810277610L;
	
	public Principals()
	{
		Role = new Roles();
		gruposUsuarios = new ArrayList<GrupoUsuariosPrincipals>();
		grupoUsuariosSelecionado = new GrupoUsuarios();
		imagem = new Arquivo();
		assinaturaEmail = new Arquivo();
		primeiroLogin = true;
		nome = "";
		cargo = new Cargo();
		distribuidor = null;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PRINCIPALS_SEQUENCE")
    private Long id;
	
	@Email
	@NotNull
	@NotEmpty
	private String principalId;
	
	@NotNull
	@NotEmpty
	private String nome;
	
	private String observacao;
	
	private String telefone;
	
	private String celular1;
	
	private String celular2;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	  @JoinColumn(name = "role_id")
	private Roles Role;
	
	@Transient
	private GrupoUsuarios grupoUsuariosSelecionado;
	
	@OneToMany(mappedBy = "principals", targetEntity = GrupoUsuariosPrincipals.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<GrupoUsuariosPrincipals> gruposUsuarios;
	
	private String Password;
	
	@Transient
	private String confirmarSenha;
	
	private boolean primeiroLogin;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	  @JoinColumn(name = "assinturaemail_id")
	private Arquivo assinaturaEmail;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	  @JoinColumn(name = "imagem_id")
	private Arquivo imagem;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	  @JoinColumn(name = "thumbnail_id")
	private Arquivo thumbnail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "cargo_id")
	private Cargo cargo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "distribuidor_id",referencedColumnName = "id", nullable=true)
	private Lead distribuidor;
	
	@Transient
	private Part imagemPart;
	
	@Transient
	private Part assinaturaPart;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalID) {
		principalId = principalID;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public Roles getRole() {
		return Role;
	}

	public void setRole(Roles role) {
		Role = role;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isPrimeiroLogin() {
		return primeiroLogin;
	}

	public void setPrimeiroLogin(boolean primeiroLogin) {
		this.primeiroLogin = primeiroLogin;
	}

	public Arquivo getImagem() {
		return imagem;
	}

	public void setImagem(Arquivo imagem) {
		this.imagem = imagem;
	}

	public Part getImagemPart() {
		return imagemPart;
	}

	public void setImagemPart(Part imagemPart) {
		this.imagemPart = imagemPart;
	}

	public List<GrupoUsuariosPrincipals> getGruposUsuarios() {
		return gruposUsuarios;
	}

	public void setGruposUsuarios(List<GrupoUsuariosPrincipals> gruposUsuarios) {
		this.gruposUsuarios = gruposUsuarios;
	}

	public GrupoUsuarios getGrupoUsuariosSelecionado() {
		return grupoUsuariosSelecionado;
	}

	public void setGrupoUsuariosSelecionado(GrupoUsuarios grupoUsuariosSelecionado) {
		this.grupoUsuariosSelecionado = grupoUsuariosSelecionado;
	}

	public Arquivo getAssinaturaEmail() {
		return assinaturaEmail;
	}

	public void setAssinaturaEmail(Arquivo assinaturaEmail) {
		this.assinaturaEmail = assinaturaEmail;
	}

	public Part getAssinaturaPart() {
		return assinaturaPart;
	}

	public void setAssinaturaPart(Part assinaturaPart) {
		this.assinaturaPart = assinaturaPart;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Arquivo getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Arquivo thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getCelular1() {
		return celular1;
	}

	public void setCelular1(String celular1) {
		this.celular1 = celular1;
	}

	public String getCelular2() {
		return celular2;
	}

	public void setCelular2(String celular2) {
		this.celular2 = celular2;
	}

	public Lead getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Lead distribuidor) {
		this.distribuidor = distribuidor;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
	
}
