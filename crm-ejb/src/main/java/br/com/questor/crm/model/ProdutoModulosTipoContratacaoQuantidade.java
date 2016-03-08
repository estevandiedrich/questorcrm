package br.com.questor.crm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@XmlRootElement
@SequenceGenerator(name="PRODUTO_MODULOS_TIPO_CONTRATACAO_QUANTIDADE_SEQUENCE", sequenceName="PRODUTO_MODULOS_TIPO_CONTRATACAO_QUANTIDADE_SEQUENCE", allocationSize=1, initialValue=1)
public class ProdutoModulosTipoContratacaoQuantidade implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5167824146494096806L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PRODUTO_MODULOS_TIPO_CONTRATACAO_QUANTIDADE_SEQUENCE")
	private Long id;
	
	@OneToMany(mappedBy = "produtoModulosTipoContratacaoQuantidade", targetEntity = ProdutoModulosSelecionados.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<ProdutoModulosSelecionados> produtosModulosSelecionados;
	@ManyToOne
	  @JoinColumn(name = "tipoContratacao_id")
	private TipoContratacao tipoContratacao;
	
	private BigDecimal quantidade;
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}
	public List<ProdutoModulosSelecionados> getProdutosModulosSelecionados() {
		return produtosModulosSelecionados;
	}

	public void setProdutosModulosSelecionados(List<ProdutoModulosSelecionados> produtosModulosSelecionados) {
		this.produtosModulosSelecionados = produtosModulosSelecionados;
	}

	public TipoContratacao getTipoContratacao() {
		return tipoContratacao;
	}

	public void setTipoContratacao(TipoContratacao tipoContratacao) {
		this.tipoContratacao = tipoContratacao;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}
}
