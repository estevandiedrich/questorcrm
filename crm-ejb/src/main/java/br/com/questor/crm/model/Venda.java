package br.com.questor.crm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Venda implements Serializable {
	public Venda()
	{
		this.cliente = new Cliente();
		this.produtos = new ArrayList<ProdutoVenda>();
		this.produtoVenda = new ProdutoVenda();
		this.dataVenda = new Date();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 7498780196599370312L;
	
	@Id
	@GeneratedValue
	private Long id;
	@OneToMany(mappedBy = "venda", targetEntity = ProdutoVenda.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ProdutoVenda> produtos;
	@ManyToOne
	  @JoinColumn(name = "cliente_id")
	private Cliente cliente;
	
	private Date dataVenda;
	@Transient
	private ProdutoVenda produtoVenda;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ProdutoVenda> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ProdutoVenda> produtos) {
		this.produtos = produtos;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ProdutoVenda getProdutoVenda() {
		return produtoVenda;
	}

	public void setProdutoVenda(ProdutoVenda produtoVenda) {
		this.produtoVenda = produtoVenda;
	}	
}
