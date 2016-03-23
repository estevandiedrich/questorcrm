package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="moduloselecionado",
	indexes = 
		{
				@Index(columnList = "produtomodulosselecionados_id",name = "produtomodulosselecionados_idx"),
				@Index(columnList = "modulo_id",name = "modulo_idx")
		}
)
@NamedQueries(value = {@NamedQuery(name = "ModuloSelecionado.findByProdutoModuloSelecionado",query = "SELECT ms FROM ModuloSelecionado ms WHERE ms.produtoModulosSelecionados.id = :produtoModulosSelecionados")})
@XmlRootElement
@SequenceGenerator(name="MODULO_SELECIONADO_SEQUENCE", sequenceName="MODULO_SELECIONADO_SEQUENCE", allocationSize=1, initialValue=1)
public class ModuloSelecionado implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2835721353798326989L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="MODULO_SELECIONADO_SEQUENCE")
	private Long id;
	@ManyToOne
	  @JoinColumn(name = "modulo_id")
	private Modulo modulo;
	@ManyToOne
	  @JoinColumn(name = "produtomodulosselecionados_id")
	private ProdutoModulosSelecionados produtoModulosSelecionados;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Modulo getModulo() {
		return modulo;
	}
	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
	public ProdutoModulosSelecionados getProdutoModulosSelecionados() {
		return produtoModulosSelecionados;
	}
	public void setProdutosModulosSelecionados(ProdutoModulosSelecionados produtoModulosSelecionados) {
		this.produtoModulosSelecionados = produtoModulosSelecionados;
	}
}
