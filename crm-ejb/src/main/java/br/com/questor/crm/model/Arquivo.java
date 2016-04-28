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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "arquivo",indexes = {
		@Index(columnList = "id", name = "arquivo_id_idx")
		}
)
@XmlRootElement
@NamedQueries(value={@NamedQuery(name="Arquivo.findById",query="SELECT a FROM Arquivo a WHERE a.id = :id")})
@SequenceGenerator(name="ARQUIVO_SEQUENCE", sequenceName="ARQUIVO_SEQUENCE", allocationSize=1, initialValue=1)
public class Arquivo implements Serializable{
	public Arquivo()
	{
		content = new byte[0];
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 3541988884281739876L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ARQUIVO_SEQUENCE")
	private Long id;
	@NotNull
	@Size(min = 1, max = 255)
	private String nome;
	private String contentType;
	private long size;
	@NotNull
	private byte[] content;
	
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
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
}
