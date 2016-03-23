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
@NamedQueries(value = {@NamedQuery(name = "GrupoUsuariosLead.findByGrupoUsuarios",query = "SELECT gul FROM GrupoUsuariosLead gul WHERE gul.grupoUsuarios IN (:grupoUsuarios)")})
@SequenceGenerator(name="GRUPO_USUARIOS_LEAD_SEQUENCE", sequenceName="GRUPO_USUARIOS_LEAD_SEQUENCE", allocationSize=1, initialValue=1)
public class GrupoUsuariosLead implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1334309480177629183L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="GRUPO_USUARIOS_LEAD_SEQUENCE")
    private Long id;
	
	@ManyToOne
	  @JoinColumn(name = "grupousuarios_id")
	private GrupoUsuarios grupoUsuarios;
	@ManyToOne
	  @JoinColumn(name = "lead_id")
	private Lead lead;
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
	public Lead getLead() {
		return lead;
	}
	public void setLead(Lead lead) {
		this.lead = lead;
	}
}
