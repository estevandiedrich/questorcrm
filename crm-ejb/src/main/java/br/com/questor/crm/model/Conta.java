package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="CONTA_SEQUENCE", sequenceName="CONTA_SEQUENCE", allocationSize=1, initialValue=1)
public class Conta extends Lead implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1151091731613794812L;
	
//	@OneToMany(mappedBy = "lead", targetEntity = Proposta.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
//	private List<Proposta> cotacoes;

//	public List<Proposta> getCotacoes() {
//		return cotacoes;
//	}
//
//	public void setCotacoes(List<Proposta> cotacoes) {
//		this.cotacoes = cotacoes;
//	}
	
	
}
