package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="CONTATO_SEQUENCE", sequenceName="CONTATO_SEQUENCE", allocationSize=1, initialValue=1)
public class Contato implements Serializable{
	public Contato()
	{
		cargo = new Cargo();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 3370841261120692829L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CONTATO_SEQUENCE")
	private Long id;
	
	@NotNull
	private String nome;
	
	private String email;
	
	private String telefone;
	
	@ManyToOne
	  @JoinColumn(name = "cargo_id")
	private Cargo cargo;	
	@ManyToOne
	  @JoinColumn(name = "lead_id")
	private Lead lead;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Lead getLead() {
		return lead;
	}

	public void setLead(Lead lead) {
		this.lead = lead;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
}
