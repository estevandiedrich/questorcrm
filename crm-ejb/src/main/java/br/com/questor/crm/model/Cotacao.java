package br.com.questor.crm.model;

import java.io.Serializable;
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

@Entity
@Table(name = "cotacao",indexes = {
		@Index(columnList = "id", name = "cotacao_id_idx"),
		@Index(columnList = "oportunidade_id", name = "cotacao_oportunidade_idx"),
		@Index(columnList = "dataehoracriacao", name = "cotacao_dataehoracriacao_idx")
		}
)
@XmlRootElement
@NamedQueries(value = {@NamedQuery(name = "Cotacao.findByOportunidade", query = "SELECT c FROM Cotacao c WHERE c.oportunidade.id = :oportunidade"),
						@NamedQuery(name = "Cotacao.findAll", query = "SELECT c FROM Cotacao c ")})
@SequenceGenerator(name="COTACAO_SEQUENCE", sequenceName="COTACAO_SEQUENCE", allocationSize=1, initialValue=1)
public class Cotacao implements Serializable{
	public Cotacao()
	{
		oportunidade = new Oportunidade();
		produtosModulosSelecionados = new ArrayList<ProdutoModulosSelecionados>();
		dataEHoraCriacao = new Date();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5383574026632126252L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="COTACAO_SEQUENCE")
	private Long id;
	
	private String descricao;
	@ManyToOne
	  @JoinColumn(name = "oportunidade_id")
	private Oportunidade oportunidade;
	
	@OneToMany(mappedBy = "cotacao", targetEntity = ProdutoModulosSelecionados.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<ProdutoModulosSelecionados> produtosModulosSelecionados;
	@Transient
	private ProdutoModulosSelecionados produtoModulosSelecionados;
	
	private Date dataEHoraCriacao;

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

	public List<ProdutoModulosSelecionados> getProdutosModulosSelecionados() {
		return produtosModulosSelecionados;
	}

	public void setProdutosModulosSelecionados(List<ProdutoModulosSelecionados> produtoModulosSelecionados) {
		this.produtosModulosSelecionados = produtoModulosSelecionados;
	}

	public Date getDataEHoraCriacao() {
		return dataEHoraCriacao;
	}

	public void setDataEHoraCriacao(Date dataEHoraCriacao) {
		this.dataEHoraCriacao = dataEHoraCriacao;
	}

	public ProdutoModulosSelecionados getProdutoModulosSelecionados() {
		return produtoModulosSelecionados;
	}

	public void setProdutoModulosSelecionados(ProdutoModulosSelecionados produtoModulosSelecionados) {
		this.produtoModulosSelecionados = produtoModulosSelecionados;
	}

	public Oportunidade getOportunidade() {
		return oportunidade;
	}

	public void setOportunidade(Oportunidade oportunidade) {
		this.oportunidade = oportunidade;
	}
}
