package br.com.questor.crm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="EMAIL_SEQUENCE", sequenceName="EMAIL_SEQUENCE", allocationSize=1, initialValue=1)
public class Email implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7399023297088588230L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="EMAIL_SEQUENCE")
	private Long id;
	@NotNull
	private String emailFrom;
	@NotNull
	private String emailTo;
	@NotNull
	private String subject;
	@NotNull
	private String text;
	@ManyToOne(cascade=CascadeType.DETACH)
    private Lead lead;   
	
	private Date sentDate;
	
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
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String to) {
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
}
