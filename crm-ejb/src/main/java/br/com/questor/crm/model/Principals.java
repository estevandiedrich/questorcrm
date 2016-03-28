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
		@NamedQuery(name = "Principals.findById",query = "SELECT p FROM Principals p JOIN FETCH p.Role WHERE p.id = :id"),
		@NamedQuery(name = "Principals.findImagemById",query = "SELECT p.imagem FROM Principals p WHERE p.id = :id"),
		@NamedQuery(name = "Principals.findAssinaturaById",query = "SELECT p.assinaturaEmail FROM Principals p WHERE p.id = :id")
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
		imagem = new Imagem();
		assinaturaEmail = new Imagem();
		primeiroLogin = true;
		nome = "";
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PRINCIPALS_SEQUENCE")
    private Long id;
	
	@NotNull
	private String PrincipalID;
	
	@NotNull
	private String nome;
	
	@NotNull
	private String email;
	
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "role_id")
	private Roles Role;
	
	@Transient
	private GrupoUsuarios grupoUsuariosSelecionado;
	
//	@ManyToMany
//    @JoinTable(name="grupousuarios_principals", joinColumns={@JoinColumn(name="principals_id")}, inverseJoinColumns={@JoinColumn(name="grupousuarios_id")})
	@OneToMany(mappedBy = "principals", targetEntity = GrupoUsuariosPrincipals.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<GrupoUsuariosPrincipals> gruposUsuarios;
	
	@NotNull
	private String Password;
	
	private boolean primeiroLogin;
	
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "assinturaemail_id")
	private Imagem assinaturaEmail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "imagem_id")
	private Imagem imagem;
	
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

	public String getPrincipalID() {
		return PrincipalID;
	}

	public void setPrincipalID(String principalID) {
		PrincipalID = principalID;
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

	public List<GrupoUsuariosPrincipals> getGruposUsuarios() {
		return gruposUsuarios;
	}

	public void setGruposUsuarios(List<GrupoUsuariosPrincipals> gruposUsuarios) {
		this.gruposUsuarios = gruposUsuarios;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public GrupoUsuarios getGrupoUsuariosSelecionado() {
		return grupoUsuariosSelecionado;
	}

	public void setGrupoUsuariosSelecionado(GrupoUsuarios grupoUsuariosSelecionado) {
		this.grupoUsuariosSelecionado = grupoUsuariosSelecionado;
	}

	public Imagem getAssinaturaEmail() {
		return assinaturaEmail;
	}

	public void setAssinaturaEmail(Imagem assinaturaEmail) {
		this.assinaturaEmail = assinaturaEmail;
	}

	public Part getAssinaturaPart() {
		return assinaturaPart;
	}

	public void setAssinaturaPart(Part assinaturaPart) {
		this.assinaturaPart = assinaturaPart;
	}
	
}
