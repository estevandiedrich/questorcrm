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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "cidade",indexes = {
		@Index(columnList = "id", name = "cidade_id_idx"),
		@Index(columnList = "uf_id", name = "cidade_uf_id_idx"),
		@Index(columnList = "nome", name = "cidade_nome_idx")
		}
)
@NamedQueries(value = {@NamedQuery(name = "Cidade.findByUF",query = "SELECT c FROM Cidade c WHERE c.uf.id = :uf")})
@XmlRootElement
@SequenceGenerator(name="CIDADE_SEQUENCE", sequenceName="CIDADE_SEQUENCE", allocationSize=1, initialValue=1)
public class Cidade implements Serializable{
	public Cidade()
	{
		uf = new UF();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4045561183795484440L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CIDADE_SEQUENCE")
	private Long id;
	@NotNull
	private String nome;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "uf_id")
	private UF uf;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public UF getUf() {
		return uf;
	}
	public void setUf(UF uf) {
		this.uf = uf;
	}
}
