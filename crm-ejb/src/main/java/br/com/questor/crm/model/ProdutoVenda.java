package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class ProdutoVenda implements Serializable {
	public ProdutoVenda()
	{
		this.produto = new Produto();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8453844827860488782L;
	@Id
	@GeneratedValue
	private Long id;
	
	private int quantidade;
//	@OneToOne(cascade = CascadeType.DETACH, optional = false, fetch = FetchType.EAGER, orphanRemoval = false)
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "produto_id")
	private Produto produto;
//	@OneToOne(cascade = CascadeType.DETACH, optional = false, fetch = FetchType.EAGER, orphanRemoval = false)
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "venda_id")
	private Venda venda;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public Venda getVenda() {
		return venda;
	}
	public void setVenda(Venda venda) {
		this.venda = venda;
	}	
}
