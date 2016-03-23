package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries(value = 
	{
			@NamedQuery(name = "GrupoUsuariosPrincipals.findByGrupoUsuarios",query = "SELECT gup.principals FROM GrupoUsuariosPrincipals gup WHERE gup.grupoUsuarios.id IN (:gruposUsuarios) "),
			@NamedQuery(name = "GrupoUsuariosPrincipals.findGrupoUsuariosByPrincipal",query = "SELECT gup.grupoUsuarios FROM GrupoUsuariosPrincipals gup WHERE gup.principals.id = :principal"),
			@NamedQuery(name = "GrupoUsuariosPrincipals.findByPrincipal",query = "SELECT gup FROM GrupoUsuariosPrincipals gup WHERE gup.principals.id = :principal")
	}
)
@SequenceGenerator(name="GRUPO_USUARIOS_PRINCIPALS_SEQUENCE", sequenceName="GRUPO_USUARIOS_PRINCIPALS_SEQUENCE", allocationSize=1, initialValue=1)
public class GrupoUsuariosPrincipals implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8276411942568790857L;
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="GRUPO_USUARIOS_PRINCIPALS_SEQUENCE")
    private Long id;
	
	@ManyToOne
	  @JoinColumn(name = "grupousuarios_id")
	private GrupoUsuarios grupoUsuarios;
	@ManyToOne
	  @JoinColumn(name = "principals_id")
	private Principals principals;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public GrupoUsuarios getGrupoUsuarios() {
		return grupoUsuarios;
	}
	public void setGrupoUsuarios(GrupoUsuarios grupoUsuarios) {
		this.grupoUsuarios = grupoUsuarios;
	}
	public Principals getPrincipals() {
		return principals;
	}
	public void setPrincipals(Principals principals) {
		this.principals = principals;
	}
}
