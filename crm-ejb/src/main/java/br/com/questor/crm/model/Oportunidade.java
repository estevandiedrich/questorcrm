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

import br.com.questor.crm.enums.FaseOportunidadeEnum;
@Entity
@Table(name = "oportunidade",indexes = {
		@Index(columnList = "id", name = "oportunidade_id_idx")
		}
)
@XmlRootElement
@NamedQueries(value = {@NamedQuery(name = "Oportunidade.findAll",query = "SELECT o FROM Oportunidade o JOIN FETCH o.conta ORDER BY o.dataAbertura")})
@SequenceGenerator(name="OPORTUNIDADE_SEQUENCE", sequenceName="OPORTUNIDADE_SEQUENCE", allocationSize=1, initialValue=1)
public class Oportunidade implements Serializable{
	public Oportunidade()
	{
		conta = new Lead();
		dataAbertura = new Date();
		contatos = new ArrayList<>();
		contatoSelecionado = new Contato();
		cotacoes = new ArrayList<Cotacao>();
		fase = FaseOportunidadeEnum.CONTATO_INICIAL;
		valorTotalServicos = BigDecimal.ZERO;
		valorTotalProdutos = BigDecimal.ZERO;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 929911489077407660L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="OPORTUNIDADE_SEQUENCE")
	private Long id;
	private String titulo;
	private String descricao;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "conta_id")
	private Lead conta;
	
	private Date dataAbertura;
	
	private Date dataValidade;
	
	private Date dataPrevistaFechamento;
	
	private String observacao;
	
	private FaseOportunidadeEnum fase;
	
	private BigDecimal valorTotalServicos;
	
	private BigDecimal valorTotalProdutos;
	
	@OneToMany(mappedBy = "oportunidade", targetEntity = OportunidadeContato.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<OportunidadeContato> contatos;
	
	@OneToMany(mappedBy = "oportunidade", targetEntity = Cotacao.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<Cotacao> cotacoes;
	
	@Transient
	private Contato contatoSelecionado;
	
	public FaseOportunidadeEnum[] faseValues()
	{
		return FaseOportunidadeEnum.values();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Cotacao> getCotacoes() {
		return cotacoes;
	}

	public void setCotacoes(List<Cotacao> cotacoes) {
		this.cotacoes = cotacoes;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Lead getConta() {
		return conta;
	}

	public void setConta(Lead conta) {
		this.conta = conta;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}

	public Date getDataPrevistaFechamento() {
		return dataPrevistaFechamento;
	}

	public void setDataPrevistaFechamento(Date dataPrevistaFechamento) {
		this.dataPrevistaFechamento = dataPrevistaFechamento;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public FaseOportunidadeEnum getFase() {
		return fase;
	}

	public void setFase(FaseOportunidadeEnum fase) {
		this.fase = fase;
	}

	public BigDecimal getValorTotalServicos() {
		return valorTotalServicos;
	}

	public void setValorTotalServicos(BigDecimal valorTotalServicos) {
		this.valorTotalServicos = valorTotalServicos;
	}

	public BigDecimal getValorTotalProdutos() {
		return valorTotalProdutos;
	}

	public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
		this.valorTotalProdutos = valorTotalProdutos;
	}

	public List<OportunidadeContato> getContatos() {
		return contatos;
	}

	public void setContatos(List<OportunidadeContato> contatos) {
		this.contatos = contatos;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Contato getContatoSelecionado() {
		return contatoSelecionado;
	}

	public void setContatoSelecionado(Contato contatoSelecionado) {
		this.contatoSelecionado = contatoSelecionado;
	}
	
}
