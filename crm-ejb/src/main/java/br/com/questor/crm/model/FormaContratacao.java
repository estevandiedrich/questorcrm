package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="FORMA_CONTRATACAO_SEQUENCE", sequenceName="FORMA_CONTRATACAO_SEQUENCE", allocationSize=1, initialValue=1)
public class FormaContratacao implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2148915698194828471L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="FORMA_CONTRATACAO_SEQUENCE")
	private Long id;
	
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
