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
@Table(name = "atividade",indexes = {
		@Index(columnList = "id", name = "atividade_id_idx")
		}
)
@XmlRootElement
@NamedQueries(value = {
		@NamedQuery(name = "Atividade.findAll",query = "SELECT a FROM Atividade a")
		}
)
@SequenceGenerator(name="ATIVIDADE_SEQUENCE", sequenceName="ATIVIDADE_SEQUENCE", allocationSize=1, initialValue=1)
public class Atividade implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3860902674929936900L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ATIVIDADE_SEQUENCE")
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
