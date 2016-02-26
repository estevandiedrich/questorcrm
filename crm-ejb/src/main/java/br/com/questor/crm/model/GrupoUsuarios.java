package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "descricao"))
@SequenceGenerator(name="GRUPO_USUARIOS_SEQUENCE", sequenceName="GRUPO_USUARIOS_SEQUENCE", allocationSize=1, initialValue=1)
public class GrupoUsuarios implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2375606029172014581L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="GRUPO_USUARIOS_SEQUENCE")
    private Long id;
	
	@NotNull
	private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
