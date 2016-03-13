package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="UF_SEQUENCE", sequenceName="UF_SEQUENCE", allocationSize=1, initialValue=1)
public class UF implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1019081800456831862L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="UF_SEQUENCE")
	private Long id;
	@NotNull
	private String nome;
	@NotNull
	private String sigla;
	@NotNull
	private String prazo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getPrazo() {
		return prazo;
	}

	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}
}
