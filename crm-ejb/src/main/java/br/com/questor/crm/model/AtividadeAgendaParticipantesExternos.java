package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
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
@Table(name = "atividadeagendaparticipantesexternos",indexes = {
		@Index(columnList = "id", name = "aape_id_idx"),
		@Index(columnList = "atividadeagenda_id", name = "aape_atividadeagenda_id_idx"),
		@Index(columnList = "participanteexterno_id", name = "aape_participanteexterno_id_idx")
		}
)
@XmlRootElement
@NamedQueries(value = {
		@NamedQuery(name = "AtividadeAgendaParticipantesExternos.findByAtividadeAgenda",query = "SELECT aape FROM AtividadeAgendaParticipantesExternos aape WHERE aape.atividadeAgenda.id = :atividadeAgenda")
})
@SequenceGenerator(name="ATIVIDADE_AGENDA_PARTICIPANTES_EXTERNOS_SEQUENCE", sequenceName="ATIVIDADE_AGENDA_PARTICIPANTES_EXTERNOS_SEQUENCE", allocationSize=1, initialValue=1)
public class AtividadeAgendaParticipantesExternos implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3739952248413143755L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ATIVIDADE_AGENDA_PARTICIPANTES_EXTERNOS_SEQUENCE")
	private Long id;
	
	@ManyToOne
	  @JoinColumn(name = "atividadeagenda_id")
	private AtividadeAgenda atividadeAgenda;
	
	@ManyToOne
	  @JoinColumn(name = "participanteexterno_id")
	private Contato participanteExterno;

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

	public Contato getParticipanteExterno() {
		return participanteExterno;
	}

	public void setParticipanteExterno(Contato participanteExterno) {
		this.participanteExterno = participanteExterno;
	}
}
