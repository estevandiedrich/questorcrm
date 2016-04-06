package br.com.questor.crm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
@Table(name = "atividadeagenda",indexes = {
		@Index(columnList = "id", name = "atividadeagenda_id_idx"),
		@Index(columnList = "principals_id", name = "atividadeagenda_principals_id_idx"),
		@Index(columnList = "lead_id", name = "atividadeagenda_lead_id_idx"),		
		@Index(columnList = "contato_id", name = "atividadeagenda_contato_id_idx")		
		}
)
@XmlRootElement
@NamedQueries(value = 
	{
			@NamedQuery(name = "AtividadeAgenda.findAll",query = "SELECT aa FROM AtividadeAgenda AS aa INNER JOIN aa.usuario AS u INNER JOIN u.Role AS r ORDER BY aa.dataEHora "),
			@NamedQuery(name = "AtividadeAgenda.findByLead",query = "SELECT aa FROM AtividadeAgenda AS aa JOIN FETCH aa.usuario WHERE aa.lead.id = :lead ORDER BY aa.dataEHora "),
			@NamedQuery(name = "AtividadeAgenda.findByPrincipal",query = "SELECT aa FROM AtividadeAgenda AS aa JOIN FETCH aa.lead JOIN FETCH aa.usuario JOIN aa.participantesInternos pi WHERE aa.usuario.id = :principal OR pi.participantesInternos.id = :principal ORDER BY aa.dataEHora ")
	}
)
@SequenceGenerator(name="ATIVIDADE_AGENDA_SEQUENCE", sequenceName="ATIVIDADE_AGENDA_SEQUENCE", allocationSize=1, initialValue=1)
public class AtividadeAgenda implements Serializable{
	public AtividadeAgenda()
	{
		this.dataEHora = new Date();
		this.hora = new Date();
		participanteExternoSelecionado = new Contato();
		participanteInternoSelecionado = new Principals();
		participantesInternos = new ArrayList<>();
		participantesExternos = new ArrayList<>();
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
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "principals_id")
	private Principals usuario;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "lead_id")
	private Lead lead;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "contato_id")
	private Contato contato;
	
	@OneToMany(mappedBy = "atividadeAgenda", targetEntity = AtividadeAgendaParticipantesInternos.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<AtividadeAgendaParticipantesInternos> participantesInternos;
	@OneToMany(mappedBy = "atividadeAgenda", targetEntity = AtividadeAgendaParticipantesExternos.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<AtividadeAgendaParticipantesExternos> participantesExternos;
	@Transient
	private Contato participanteExternoSelecionado;
	@Transient
	private Principals participanteInternoSelecionado;
	private String antecedencia;
	
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

	public String getAntecedencia() {
		return antecedencia;
	}

	public void setAntecedencia(String antecedencia) {
		this.antecedencia = antecedencia;
	}

	public List<AtividadeAgendaParticipantesInternos> getParticipantesInternos() {
		return participantesInternos;
	}

	public void setParticipantesInternos(List<AtividadeAgendaParticipantesInternos> participantesInternos) {
		this.participantesInternos = participantesInternos;
	}

	public List<AtividadeAgendaParticipantesExternos> getParticipantesExternos() {
		return participantesExternos;
	}

	public void setParticipantesExternos(List<AtividadeAgendaParticipantesExternos> participantesExternos) {
		this.participantesExternos = participantesExternos;
	}

	public Contato getParticipanteExternoSelecionado() {
		return participanteExternoSelecionado;
	}

	public void setParticipanteExternoSelecionado(Contato participanteExternoSelecionado) {
		this.participanteExternoSelecionado = participanteExternoSelecionado;
	}

	public Principals getParticipanteInternoSelecionado() {
		return participanteInternoSelecionado;
	}

	public void setParticipanteInternoSelecionado(Principals participanteInternoSelecionado) {
		this.participanteInternoSelecionado = participanteInternoSelecionado;
	}
}
