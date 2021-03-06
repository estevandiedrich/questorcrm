package br.com.questor.crm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "email",indexes = {
		@Index(columnList = "id", name = "email_id_idx"),
		@Index(columnList = "lead_id", name = "email_lead_id_idx"),
		@Index(columnList = "emailFrom", name = "emailFrom_idx")
		}
)
@XmlRootElement
@NamedQueries(value = {
		@NamedQuery(name = "Email.findByLead", query = "SELECT e FROM Email e WHERE e.lead.id = :lead"),
		@NamedQuery(name = "Email.findByEmail", query = "SELECT DISTINCT e FROM Email e JOIN FETCH e.emailTo WHERE e.emailFrom = :email")
		}
)
@SequenceGenerator(name="EMAIL_SEQUENCE", sequenceName="EMAIL_SEQUENCE", allocationSize=1, initialValue=1)
public class Email implements Serializable{
	public Email()
	{
		lead = new Lead();
		contatoSelecionado = new Contato();
		anexos = new ArrayList<Anexo>();
		emailTo = new ArrayList<>();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -7399023297088588230L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="EMAIL_SEQUENCE")
	private Long id;
	@NotNull
	private String emailFrom;
	@OneToMany(mappedBy = "email", targetEntity = ContatoEmail.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<ContatoEmail> emailTo;
	@NotNull
	private String subject;
	@NotNull
	@Column(length=2000)
	private String text;
	@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "lead_id")
    private Lead lead;   
	
	private Date sentDate;
	
	@OneToMany(mappedBy = "email", targetEntity = Anexo.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<Anexo> anexos;
	
	@Transient
	private String[] selectedTo;
	
	@Transient
	private Contato contatoSelecionado;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailFrom(String from) {
		this.emailFrom = from;
	}
	public List<ContatoEmail> getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(List<ContatoEmail> to) {
		this.emailTo = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	public Lead getLead() {
		return lead;
	}
	public void setLead(Lead lead) {
		this.lead = lead;
	}
	public String[] getSelectedTo() {
		return selectedTo;
	}
	public void setSelectedTo(String[] selectedTo) {
		this.selectedTo = selectedTo;
	}
	public List<Anexo> getAnexos() {
		return anexos;
	}
	public void setAnexos(List<Anexo> anexos) {
		this.anexos = anexos;
	}
	public Contato getContatoSelecionado() {
		return contatoSelecionado;
	}
	public void setContatoSelecionado(Contato contatoSelecionado) {
		this.contatoSelecionado = contatoSelecionado;
	}	
}
