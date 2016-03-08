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
@SequenceGenerator(name="PROPOSTA_SEQUENCE", sequenceName="PROPOSTA_SEQUENCE", allocationSize=1, initialValue=1)
public class Proposta implements Serializable{
	public Proposta()
	{
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5383574026632126252L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PROPOSTA_SEQUENCE")
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
