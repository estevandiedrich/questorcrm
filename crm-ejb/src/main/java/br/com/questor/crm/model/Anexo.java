package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="ANEXO_SEQUENCE", sequenceName="ANEXO_SEQUENCE", allocationSize=1, initialValue=1)
public class Anexo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5440575688285130916L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ANEXO_SEQUENCE")
	private Long id;
	
	private String descricao;
	
	private String contentType;
	
	private long size;
	@NotNull
	private Imagem imagem;
	
	@Transient
	private Part part;
	
	@ManyToOne
	  @JoinColumn(name = "lead_id")
	private Lead lead;

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

	public Imagem getImagem() {
		return imagem;
	}

	public void setImagem(Imagem imagem) {
		this.imagem = imagem;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public Lead getLead() {
		return lead;
	}

	public void setLead(Lead lead) {
		this.lead = lead;
	}	
}