package br.com.questor.crm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="COTACAO_SEQUENCE", sequenceName="COTACAO_SEQUENCE", allocationSize=1, initialValue=1)
public class Cotacao implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5383574026632126252L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="COTACAO_SEQUENCE")
	private Long id;
	
	private String texto;
	
	private Produto produto;
	
	private Date dataEHora;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Date getDataEHora() {
		return dataEHora;
	}

	public void setDataEHora(Date dataEHora) {
		this.dataEHora = dataEHora;
	}
}
