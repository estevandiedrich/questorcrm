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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="PRODUTO_SEQUENCE", sequenceName="PRODUTO_SEQUENCE", allocationSize=1, initialValue=1)
public class Produto implements Serializable {
	public Produto()
	{
		tipoProduto = new TiposProduto();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -3023057897109920165L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PRODUTO_SEQUENCE")
	private Long id;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String descricao;
	
	@NotNull	
	private BigDecimal valorInicial;
	
	@NotNull	
	private BigDecimal valorAtual;
	
	@ManyToOne
	  @JoinColumn(name = "tipoproduto_id")
	private TiposProduto tipoProduto;

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

	public TiposProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TiposProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	public BigDecimal getValorAtual() {
		return valorAtual;
	}

	public void setValorAtual(BigDecimal valorAtual) {
		this.valorAtual = valorAtual;
	}
}
