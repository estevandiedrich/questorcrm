package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "propriedades",indexes = {
		@Index(columnList = "id", name = "propriedades_id_idx")
		},
	uniqueConstraints = @UniqueConstraint(columnNames = "chave")
)
@XmlRootElement
@NamedQueries(value={
		@NamedQuery(name = "Propriedades.findByChave",query = "SELECT p FROM Propriedades p WHERE p.chave = :chave")
		}
)
@SequenceGenerator(name="PROPRIEDADES_SEQUENCE", sequenceName="PROPRIEDADES_SEQUENCE", allocationSize=1, initialValue=1)
public class Propriedades implements Serializable{
	public final static String EMAIL_RECUPERACAO_SENHA = "EMAIL_RECUPERACAO_SENHA";
	public final static String EMAIL_NOVO_USUARIO = "EMAIL_NOVO_USUARIO";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1851154097360293359L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PROPRIEDADES_SEQUENCE")
    private Long id;
	@NotNull
	private String chave;
	@NotNull
	private String valor;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
}
