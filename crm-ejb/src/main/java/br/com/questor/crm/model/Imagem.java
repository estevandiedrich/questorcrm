package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="IMAGEM_SEQUENCE", sequenceName="IMAGEM_SEQUENCE", allocationSize=1, initialValue=1)
public class Imagem implements Serializable{
	public Imagem()
	{
		imagem = new byte[0];
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 3541988884281739876L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="IMAGEM_SEQUENCE")
	private Long id;
	@NotNull
	@Size(min = 1, max = 255)
	private String nome;
	private String contentType;
	private long size;
	@NotNull
	private byte[] imagem;
	
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
	public byte[] getImagem() {
		return imagem;
	}
	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
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
