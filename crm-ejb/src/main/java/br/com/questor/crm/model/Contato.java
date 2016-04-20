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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "contato",indexes = {
		@Index(columnList = "id", name = "contato_id_idx"),
		@Index(columnList = "lead_id", name = "contato_lead_id_idx"),
		@Index(columnList = "cargo_id", name = "contato_cargo_id_idx")
		}
)
@XmlRootElement
@NamedQueries(value={
		@NamedQuery(name = "Contato.findByLead",query="SELECT c FROM Contato c JOIN FETCH c.cargo WHERE c.lead.id = :lead"),
		@NamedQuery(name = "Contato.findById",query="SELECT c FROM Contato c JOIN FETCH c.cargo WHERE c.id = :id")
		})
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
	@Email
	@NotNull
	private String email;
	
	private String telefone;
	
	private String celular;
	
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "cargo_id")
	private Cargo cargo;	
	@ManyToOne(fetch = FetchType.LAZY)
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

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}
}
