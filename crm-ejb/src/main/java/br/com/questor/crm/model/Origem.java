package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "origem",indexes = {
		@Index(columnList = "id", name = "origem_id_idx")
		}
)
@XmlRootElement
@NamedQueries(value = {
		@NamedQuery(name = "Origem.findAll",query = "SELECT o FROM Origem o")
		}
)
@SequenceGenerator(name="ORIGEM_SEQUENCE", sequenceName="ORIGEM_SEQUENCE", allocationSize=1, initialValue=1)
public class Origem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6858214445805270269L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ORIGEM_SEQUENCE")
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
