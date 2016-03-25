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
@Table(name = "cargo",indexes = {
		@Index(columnList = "id", name = "cargo_id_idx")
		}
)
@XmlRootElement
@NamedQueries(value = {@NamedQuery(name = "Cargo.findAll",query = "SELECT c FROM Cargo c ORDER BY c.descricao")})
@SequenceGenerator(name="CARGO_SEQUENCE", sequenceName="CARGO_SEQUENCE", allocationSize=1, initialValue=1)
public class Cargo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4274617932625913069L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CARGO_SEQUENCE")
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
