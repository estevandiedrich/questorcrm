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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "principalid"))
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
	
	@ManyToOne
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
	
	@ManyToOne
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
	
}
