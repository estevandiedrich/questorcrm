package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="LEAD_GROUP_SEQUENCE", sequenceName="LEAD_GROUP_SEQUENCE", allocationSize=1, initialValue=1)
public class LeadGroup implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3726955378185563420L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="LEAD_GROUP_SEQUENCE")
	private Long id;
	
	@NotNull
	private String descricao;
	
	@ManyToOne
	  @JoinColumn(name = "lead_id")
	private Lead lead;

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

	public Lead getLead() {
		return lead;
	}

	public void setLead(Lead lead) {
		this.lead = lead;
	}	
}
