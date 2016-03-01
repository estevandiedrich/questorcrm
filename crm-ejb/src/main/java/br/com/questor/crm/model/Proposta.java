package br.com.questor.crm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="PROPOSTA_SEQUENCE", sequenceName="PROPOSTA_SEQUENCE", allocationSize=1, initialValue=1)
public class Proposta implements Serializable{
	public Proposta()
	{
		this.negociacoes = new ArrayList<Negociacao>();
		this.produtos = new ArrayList<Produto>();
		this.produto = new Produto();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5383574026632126252L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PROPOSTA_SEQUENCE")
	private Long id;
	
	private String descricao;
	
	private BigDecimal valorInicial;
	
	@OneToMany(mappedBy = "cotacao", targetEntity = Negociacao.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)	
	private List<Negociacao> negociacoes;
	
	@ManyToMany
    @JoinTable(name="produto_cotacao", joinColumns={@JoinColumn(name="cotacao_id")}, inverseJoinColumns={@JoinColumn(name="produto_id")})
	private List<Produto> produtos;
	@ManyToOne
	  @JoinColumn(name = "lead_id")
	private Lead lead;
	
	public Lead getLead() {
		return lead;
	}

	public void setLead(Lead lead) {
		this.lead = lead;
	}
	private Date dataEHoraCriacao;
	
	@Transient
	private Produto produto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProduto(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Date getDataEHoraCriacao() {
		return dataEHoraCriacao;
	}

	public void setDataEHoraCriacao(Date dataEHoraCriacao) {
		this.dataEHoraCriacao = dataEHoraCriacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	public List<Negociacao> getNegociacoes() {
		return negociacoes;
	}

	public void setNegociacoes(List<Negociacao> negociacoes) {
		this.negociacoes = negociacoes;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}	
	
}
