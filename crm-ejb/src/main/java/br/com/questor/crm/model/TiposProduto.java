package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="TIPO_PRODUTO_SEQUENCE", sequenceName="TIPO_PRODUTO_SEQUENCE", allocationSize=1, initialValue=1)
public class TiposProduto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3438451684658703603L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="TIPO_PRODUTO_SEQUENCE")
	private Long id;
	
	@NotNull
	@Size(min = 1, max = 255)
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
