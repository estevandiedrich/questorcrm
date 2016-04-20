package br.com.questor.crm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@Table(name = "produtomodulosselecionados",indexes = {
		@Index(columnList = "id", name = "produtomodulosselecionados_id_idx"),
		@Index(columnList = "cotacao_id", name = "produtomodulosselecionados_cotacao_idx"),
		@Index(columnList = "produto_id", name = "produtomodulosselecionados_produto_idx"),
		@Index(columnList = "tipocontratacao_id", name = "produtomodulosselecionados_tipocontratacao_idx")
		}
)
@XmlRootElement
@NamedQueries(value = {
		@NamedQuery(name = "ProdutoModulosSelecionados.findByCotacao",query = "SELECT DISTINCT p FROM ProdutoModulosSelecionados p JOIN FETCH p.produto JOIN FETCH p.modulosSelecionados JOIN FETCH p.tipoContratacao WHERE p.cotacao.id = :cotacao")
		}
)
@SequenceGenerator(name="PRODUTO_MODULOS_SELECIONADOS_SEQUENCE", sequenceName="PRODUTO_MODULOS_SELECIONADOS_SEQUENCE", allocationSize=1, initialValue=1)
public class ProdutoModulosSelecionados implements Serializable{
	public ProdutoModulosSelecionados()
	{
		this.produto = new Produto();
		this.moduloSelecionado = new Modulo();
		this.modulosSelecionados = new ArrayList<ModuloSelecionado>();
		this.tipoContratacao = new TipoContratacao();
		this.quantidade = BigDecimal.ZERO;
		this.valorUnitario = BigDecimal.ZERO;
		this.valorTotal = BigDecimal.ZERO;
		this.cotacao = new Cotacao();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4640034516077096205L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PRODUTO_MODULOS_SELECIONADOS_SEQUENCE")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "cotacao_id")
	private Cotacao cotacao;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "produto_id")
	private Produto produto;
	@OneToMany(mappedBy = "produtoModulosSelecionados", targetEntity = ModuloSelecionado.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<ModuloSelecionado> modulosSelecionados;
	@Transient
	private Modulo moduloSelecionado;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "tipocontratacao_id")
	private TipoContratacao tipoContratacao;
	
	private BigDecimal quantidade;
	@Column(length=13,precision=2)
	private BigDecimal valorUnitario;
	@Column(length=13,precision=2)
	private BigDecimal valorTotal;
	
	private String observacao;
	
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

	public Cotacao getCotacao() {
		return cotacao;
	}

	public void setProposta(Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}
}
