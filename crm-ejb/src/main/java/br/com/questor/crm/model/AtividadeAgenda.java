package br.com.questor.crm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="ATIVIDADE_AGENDA_SEQUENCE", sequenceName="ATIVIDADE_AGENDA_SEQUENCE", allocationSize=1, initialValue=1)
public class AtividadeAgenda implements Serializable{
	public AtividadeAgenda()
	{
		this.dataEHora = new Date();
		this.hora = new Date();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 3632126466466287937L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ATIVIDADE_AGENDA_SEQUENCE")
	private Long id;
	@NotNull
	private String lembrete;
	@Transient
	private Date hora;
	@NotNull
	private Date dataEHora;
	@ManyToOne
	  @JoinColumn(name = "principals_id")
	private Principals usuario;
	@ManyToOne
	  @JoinColumn(name = "lead_id")
	private Lead lead;
	@ManyToOne
	  @JoinColumn(name = "contato_id")
	private Contato contato;
	
	private String participantes;
	@Transient
	private String[] participantesSelecionados;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLembrete() {
		return lembrete;
	}

	public void setLembrete(String lembrete) {
		this.lembrete = lembrete;
	}

	public Date getDataEHora() {
		return dataEHora;
	}

	public void setDataEHora(Date dataEHora) {
		this.dataEHora = dataEHora;
	}

	public Principals getUsuario() {
		return usuario;
	}

	public void setUsuario(Principals usuario) {
		this.usuario = usuario;
	}

	public Lead getLead() {
		return lead;
	}

	public void setLead(Lead lead) {
		this.lead = lead;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public String getParticipantes() {
		return participantes;
	}

	public void setParticipantes(String participantes) {
		this.participantes = participantes;
	}

	public String[] getParticipantesSelecionados() {
		return participantesSelecionados;
	}

	public void setParticipantesSelecionados(String[] participantesSelecionados) {
		this.participantesSelecionados = participantesSelecionados;
	}
	
}
