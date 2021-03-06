package br.com.questor.crm.model;

import java.io.Serializable;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="modulo",
	indexes = 
		{
				@Index(columnList = "id",name = "modulo_idx"),
				@Index(columnList = "produto_id",name = "modulo_produto_idx")
		}
	)
@XmlRootElement
@NamedQueries(value={
		@NamedQuery(name = "Modulo.findByProduto",query = "SELECT m FROM Modulo m WHERE m.produto.id = :produto ORDER BY m.descricao"),
		@NamedQuery(name = "Modulo.findAll",query = "SELECT m FROM Modulo m ORDER BY m.descricao"),
		@NamedQuery(name = "Modulo.findAllComProduto",query = "SELECT m FROM Modulo m LEFT JOIN FETCH m.produto ORDER BY m.descricao"),
		@NamedQuery(name = "Modulo.findDisponiveis",query = "SELECT m FROM Modulo m WHERE m.produto IS NULL ORDER BY m.descricao")
		}
)
@SequenceGenerator(name="MODULO_SEQUENCE", sequenceName="MODULO_SEQUENCE", allocationSize=1, initialValue=1)
public class Modulo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4843547446981417034L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="MODULO_SEQUENCE")
	private Long id;
	
	private String descricao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "produto_id")
	private Produto produto;
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
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}
