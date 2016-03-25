package br.com.questor.crm.model;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "nota",indexes = {
		@Index(columnList = "id", name = "nota_id_idx"),
		@Index(columnList = "lead_id", name = "nota_lead_id_idx")
		}
)
@XmlRootElement
@NamedQueries(value = {
		@NamedQuery(name = "Nota.findByLead",query = "SELECT n FROM Nota n WHERE n.lead.id = :lead ORDER BY n.dataEHora DESC"),
		@NamedQuery(name = "Nota.findAll",query = "SELECT n FROM Nota n")
		}
)
@SequenceGenerator(name="NOTA_SEQUENCE", sequenceName="NOTA_SEQUENCE", allocationSize=1, initialValue=1)
public class Nota implements Serializable{
	public Nota()
	{
		this.lead = new Lead();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8591898484223479355L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="NOTA_SEQUENCE")
	private Long id;
	private String texto;
	private String descricao;
	private Date dataEHora;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "lead_id")
	private Lead lead;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Date getDataEHora() {
		return dataEHora;
	}
	public void setDataEHora(Date dataEHora) {
		this.dataEHora = dataEHora;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Lead getLead() {
		return lead;
	}
	public void setLead(Lead lead) {
		this.lead = lead;
	}
}
