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
@NamedQueries(value = {@NamedQuery(name = "OportunidadeContato.findByOportunidade",query = "SELECT oc FROM OportunidadeContato oc WHERE oc.oportunidade.id = :oportunidade")})
@SequenceGenerator(name="OPORTUNIDADE_CONTATO_SEQUENCE", sequenceName="OPORTUNIDADE_CONTATO_SEQUENCE", allocationSize=1, initialValue=1)
public class OportunidadeContato implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2258226678717640627L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="OPORTUNIDADE_CONTATO_SEQUENCE")
	private Long id;
	@ManyToOne
	  @JoinColumn(name = "oportunidade_id")
	private Oportunidade oportunidade;
	@ManyToOne
	  @JoinColumn(name = "contato_id")
	private Contato contato;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Oportunidade getOportunidade() {
		return oportunidade;
	}
	public void setOportunidade(Oportunidade oportunidade) {
		this.oportunidade = oportunidade;
	}
	public Contato getContato() {
		return contato;
	}
	public void setContato(Contato contato) {
		this.contato = contato;
	}
}
