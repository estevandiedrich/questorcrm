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
@XmlRootElement
@Table(name = "contatoemail",indexes = {
		@Index(columnList = "id", name = "contatoemail_id_idx"),
		@Index(columnList = "contato_id", name = "contatoemail_contato_id_idx"),
		@Index(columnList = "email_id", name = "contatoemail_email_id_idx")
		}
)
@NamedQueries(value = {
		@NamedQuery(name = "ContatoEmail.findByEmail", query = "SELECT ce FROM ContatoEmail ce WHERE ce.email.id = :email")		
		}
)
@SequenceGenerator(name="CONTATO_EMAIL_SEQUENCE", sequenceName="CONTATO_EMAIL_SEQUENCE", allocationSize=1, initialValue=1)
public class ContatoEmail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6607738357099332376L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CONTATO_EMAIL_SEQUENCE")
	private Long id;
	@ManyToOne
		@JoinColumn(name = "contato_id")
	private Contato contato;
	@ManyToOne
		@JoinColumn(name = "email_id")
	private Email email;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Contato getContato() {
		return contato;
	}
	public void setContato(Contato contato) {
		this.contato = contato;
	}
	public Email getEmail() {
		return email;
	}
	public void setEmail(Email email) {
		this.email = email;
	}
	@Override
	public String toString()
	{
		return contato.getEmail();
	}
}
