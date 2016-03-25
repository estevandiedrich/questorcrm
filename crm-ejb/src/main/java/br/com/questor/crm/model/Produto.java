package br.com.questor.crm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.questor.crm.enums.TipoProdutoEnum;

@Entity
@Table(name = "produto",indexes = {
		@Index(columnList = "id", name = "produto_id_idx")
		}
)
@XmlRootElement
@SequenceGenerator(name="PRODUTO_SEQUENCE", sequenceName="PRODUTO_SEQUENCE", allocationSize=1, initialValue=1)
public class Produto implements Serializable {
	public Produto()
	{
		modulos = new ArrayList<Modulo>();
		modulo = new Modulo();
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
	private Modulo modulo;
	
//	@OneToMany(mappedBy = "produto", targetEntity = Modulo.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="produto_modulo", joinColumns={@JoinColumn(name="produto_id")}, inverseJoinColumns={@JoinColumn(name="modulo_id")})
	private List<Modulo> modulos;
	
	private TipoProdutoEnum tipoProduto;
	
	public TipoProdutoEnum[] getTipoProdutoValues()
	{
		return TipoProdutoEnum.values();
	}

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

	public List<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public TipoProdutoEnum getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProdutoEnum tipoProduto) {
		this.tipoProduto = tipoProduto;
	}	
	
}
