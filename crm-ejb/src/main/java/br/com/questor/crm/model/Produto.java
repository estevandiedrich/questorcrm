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
		servico = new Servico();
		valor = BigDecimal.ZERO;
		servicos = new ArrayList<Servico>();
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
	
	@Transient
	private Servico servico;
	
	@NotNull	
	private BigDecimal valor;
	
	@ManyToOne
	  @JoinColumn(name = "tipoproduto_id")
	private TiposProduto tipoProduto;
	
	@OneToMany(mappedBy = "produto", targetEntity = Servico.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<Servico> servicos;

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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public List<Servico> getServicos() {
		return servicos;
	}

	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}
	
}
