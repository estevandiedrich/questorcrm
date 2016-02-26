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
@SequenceGenerator(name="SERVICO_SEQUENCE", sequenceName="SERVICO_SEQUENCE", allocationSize=1, initialValue=1)
public class Servico implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7131326708234989082L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SERVICO_SEQUENCE")
	private Long id;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String descricao;
	
	@NotNull	
	private BigDecimal valorInicial;
	
	@NotNull	
	private BigDecimal valorAtual;
	
	@ManyToOne
	  @JoinColumn(name = "tiposervico_id")
	private TipoServico tipoServico;

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

	public TipoServico getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}
}
