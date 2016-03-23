package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
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
