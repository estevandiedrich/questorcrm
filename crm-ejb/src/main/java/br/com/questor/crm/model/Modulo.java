package br.com.questor.crm.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
@SequenceGenerator(name="MODULO_SEQUENCE", sequenceName="MODULO_SEQUENCE", allocationSize=1, initialValue=1)
public class Modulo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4843547446981417034L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="MODULO_SEQUENCE")
	private Long id;
	
	private String descricao;
	
	private BigDecimal valor;
	
	@ManyToOne
	  @JoinColumn(name = "produto_id")
	private Produto produto;
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
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}
