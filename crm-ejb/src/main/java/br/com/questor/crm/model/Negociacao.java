package br.com.questor.crm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@XmlRootElement
@SequenceGenerator(name="NEGOCIACAO_SEQUENCE", sequenceName="NEGOCIACAO_SEQUENCE", allocationSize=1, initialValue=1)
public class Negociacao implements Serializable{
	public Negociacao()
	{
		this.cotacao = new Proposta();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8591898484223479355L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="NEGOCIACAO_SEQUENCE")
	private Long id;
	private String texto;
	private BigDecimal valor;
	private Date dataEHora;
	@ManyToOne
	  @JoinColumn(name = "cotacao_id")
	private Proposta cotacao;
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
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Proposta getCotacao() {
		return cotacao;
	}
	public void setCotacao(Proposta cotacao) {
		this.cotacao = cotacao;
	}
	public Date getDataEHora() {
		return dataEHora;
	}
	public void setDataEHora(Date dataEHora) {
		this.dataEHora = dataEHora;
	}
	
}
