package br.com.questor.crm.model;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="PROPOSTA_SEQUENCE", sequenceName="PROPOSTA_SEQUENCE", allocationSize=1, initialValue=1)
public class Proposta implements Serializable{
	public Proposta()
	{
		lead = new Lead();
		produtoModulosSelecionados = new ArrayList<ProdutoModulosSelecionados>();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5383574026632126252L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PROPOSTA_SEQUENCE")
	private Long id;
	
	private String descricao;
	@ManyToOne
	  @JoinColumn(name = "lead_id")
	private Lead lead;
	@OneToMany(mappedBy = "proposta", targetEntity = ProdutoModulosSelecionados.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<ProdutoModulosSelecionados> produtoModulosSelecionados;

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

	public Lead getLead() {
		return lead;
	}

	public void setLead(Lead lead) {
		this.lead = lead;
	}

	public List<ProdutoModulosSelecionados> getProdutoModulosSelecionados() {
		return produtoModulosSelecionados;
	}

	public void setProdutoModulosSelecionados(List<ProdutoModulosSelecionados> produtoModulosSelecionados) {
		this.produtoModulosSelecionados = produtoModulosSelecionados;
	}
}
