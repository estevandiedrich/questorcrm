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
@Table(name = "atividadeagendaparticipantesinternos",indexes = {
		@Index(columnList = "id", name = "aapi_idx"),
		@Index(columnList = "atividadeagenda_id", name = "aapi_atividadeagenda_id_idx"),
		@Index(columnList = "participantesinternos_id", name = "aapi_participantesinternos_id_idx")
		}
)
@XmlRootElement
@NamedQueries(value = {
		@NamedQuery(name = "AtividadeAgendaParticipantesInternos.findByAtividadeAgenda",query = "SELECT aapi FROM AtividadeAgendaParticipantesInternos aapi WHERE aapi.atividadeAgenda.id = :atividadeAgenda")
})
@SequenceGenerator(name="ATIVIDADE_AGENDA_PARTICIPANTES_INTERNOS_SEQUENCE", sequenceName="ATIVIDADE_AGENDA_PARTICIPANTES_INTERNOS_SEQUENCE", allocationSize=1, initialValue=1)
public class AtividadeAgendaParticipantesInternos implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5516463172346712469L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ATIVIDADE_AGENDA_PARTICIPANTES_INTERNOS_SEQUENCE")
	private Long id;
	@ManyToOne
	  @JoinColumn(name = "atividadeagenda_id")
	private AtividadeAgenda atividadeAgenda;
	@ManyToOne
	  @JoinColumn(name = "participantesinternos_id")
	private Principals participantesInternos;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AtividadeAgenda getAtividadeAgenda() {
		return atividadeAgenda;
	}
	public void setAtividadeAgenda(AtividadeAgenda atividadeAgenda) {
		this.atividadeAgenda = atividadeAgenda;
	}
	public Principals getParticipantesInternos() {
		return participantesInternos;
	}
	public void setParticipantesInternos(Principals participantesInternos) {
		this.participantesInternos = participantesInternos;
	}
}
