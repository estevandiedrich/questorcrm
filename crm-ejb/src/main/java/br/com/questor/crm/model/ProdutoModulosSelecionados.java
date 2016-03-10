package br.com.questor.crm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@XmlRootElement
@SequenceGenerator(name="PRODUTO_MODULOS_SELECIONADOS_SEQUENCE", sequenceName="PRODUTO_MODULOS_SELECIONADOS_SEQUENCE", allocationSize=1, initialValue=1)
public class ProdutoModulosSelecionados implements Serializable{
	public ProdutoModulosSelecionados()
	{
		this.produto = new Produto();
		this.moduloSelecionado = new Modulo();
		this.modulosSelecionados = new ArrayList<ModuloSelecionado>();
		this.tipoContratacao = new TipoContratacao();
		this.quantidade = BigDecimal.ZERO;
		this.proposta = new Proposta();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4640034516077096205L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PRODUTO_MODULOS_SELECIONADOS_SEQUENCE")
	private Long id;
	@ManyToOne
	  @JoinColumn(name = "proposta_id")
	private Proposta proposta;
	@ManyToOne
	  @JoinColumn(name = "produto_id")
	private Produto produto;
	@OneToMany(mappedBy = "produtoModulosSelecionados", targetEntity = ModuloSelecionado.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<ModuloSelecionado> modulosSelecionados;
	@Transient
	private Modulo moduloSelecionado;
	@ManyToOne
	  @JoinColumn(name = "tipocontratacao_id")
	private TipoContratacao tipoContratacao;
	
	private BigDecimal quantidade;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<ModuloSelecionado> getModulosSelecionados() {
		return modulosSelecionados;
	}

	public void setModulosSelecionados(List<ModuloSelecionado> modulosSelecionados) {
		this.modulosSelecionados = modulosSelecionados;
	}

	public Modulo getModuloSelecionado() {
		return moduloSelecionado;
	}

	public void setModuloSelecionado(Modulo moduloSelecionado) {
		this.moduloSelecionado = moduloSelecionado;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public TipoContratacao getTipoContratacao() {
		return tipoContratacao;
	}

	public void setTipoContratacao(TipoContratacao tipoContratacao) {
		this.tipoContratacao = tipoContratacao;
	}

	public Proposta getProposta() {
		return proposta;
	}

	public void setProposta(Proposta proposta) {
		this.proposta = proposta;
	}	
}
