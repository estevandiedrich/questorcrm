package br.com.questor.crm.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.data.AtividadeAgendaListProducer;
import br.com.questor.crm.data.ContatoListProducer;
import br.com.questor.crm.data.PrincipalsListProducer;
import br.com.questor.crm.model.AtividadeAgenda;
import br.com.questor.crm.model.AtividadeAgendaParticipantesExternos;
import br.com.questor.crm.model.AtividadeAgendaParticipantesInternos;
import br.com.questor.crm.model.Contato;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.Principals;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarAtividadeAgenda {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private LoginBean loginBean;
	
	@Inject
	private Event<AtividadeAgenda> atividadeAgendaEventSrc;
	
	@Inject
	private AtividadeAgendaListProducer atividadeAgendaListProducer;
	
	@Inject
	private ContatoListProducer contatoListProducer;
	
	@Inject
	private PrincipalsListProducer principalsListProducer;
	
	private AtividadeAgenda newAtividadeAgenda;
	
	private Lead leadSelecionada;
	
	public void adicionarAtividadeAgenda()
	{
		GregorianCalendar dataEHora = new GregorianCalendar();
		dataEHora.setTime(newAtividadeAgenda.getDataEHora());
		GregorianCalendar hora = new GregorianCalendar();
		hora.setTime(newAtividadeAgenda.getHora());
		dataEHora.set(Calendar.HOUR_OF_DAY, hora.get(Calendar.HOUR_OF_DAY));
		dataEHora.set(Calendar.MINUTE, hora.get(Calendar.MINUTE));
		dataEHora.set(Calendar.SECOND, hora.get(Calendar.SECOND));
		newAtividadeAgenda.setDataEHora(dataEHora.getTime());
		newAtividadeAgenda.setUsuario(loginBean.getPrincipalsFromDB());
		newAtividadeAgenda.getLead().getAtividadesAgenda().add(newAtividadeAgenda);
		try {
			salvar();
			newAtividadeAgenda.setLead(leadSelecionada);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long[] getDataEHoraAtividadesAgenda()
	{
		if(newAtividadeAgenda.getLead() == null)
		{
			newAtividadeAgenda.setLead(new Lead());
		}
		long[] dataEHoraAtividadesAgenda = new long[newAtividadeAgenda.getLead().getAtividadesAgenda().size()];
		int i = 0;
		for(AtividadeAgenda atividadeAgenda:newAtividadeAgenda.getLead().getAtividadesAgenda())
		{
			dataEHoraAtividadesAgenda[i++] = atividadeAgenda.getDataEHora().getTime();
		}
		return dataEHoraAtividadesAgenda;
	}
	
	public void setLead()
	{
		if(newAtividadeAgenda.getLead().getId() != null)
		{
			leadSelecionada = em.find(Lead.class, newAtividadeAgenda.getLead().getId());
			List<Contato> contatos = contatoListProducer.retrieveAllContatosByLeadOrderedByNome(leadSelecionada);
			if(contatos == null)
				contatos = new ArrayList<Contato>();
			leadSelecionada.setContatos(contatos);
			List<AtividadeAgenda> atividadesAgenda = atividadeAgendaListProducer.retrieveAllAtividadeAgendasByLeadOrderedByDataEHora(leadSelecionada);
			leadSelecionada.setAtividadesAgenda(atividadesAgenda);
			newAtividadeAgenda.setLead(leadSelecionada);
		}
	}
	
	@Produces
	@Named
	public AtividadeAgenda getNewAtividadeAgenda() {
		return newAtividadeAgenda;
	}
	public void avisarComAntecedenciaHandler(AjaxBehaviorEvent e)
	{
		log.info("avisar com antecedencia");
	}
	public void salvar() throws Exception {
		log.info("Salvando AtividadeAgenda" + newAtividadeAgenda.getLembrete());
		em.persist(newAtividadeAgenda);
		List<AtividadeAgendaParticipantesExternos> participantesExternos = new ArrayList<>();
		for(AtividadeAgendaParticipantesExternos participanteExterno:newAtividadeAgenda.getParticipantesExternos())
		{
			participanteExterno.setAtividadeAgenda(newAtividadeAgenda);
			em.persist(participanteExterno);
		}
		newAtividadeAgenda.setParticipantesExternos(participantesExternos);
		List<AtividadeAgendaParticipantesInternos> participantesInternos = new ArrayList<>();
		for(AtividadeAgendaParticipantesInternos participanteInterno:newAtividadeAgenda.getParticipantesInternos())
		{
			participanteInterno.setAtividadeAgenda(newAtividadeAgenda);
			em.persist(participanteInterno);
		}
		newAtividadeAgenda.setParticipantesInternos(participantesInternos);
		em.merge(newAtividadeAgenda.getLead());
		atividadeAgendaEventSrc.fire(newAtividadeAgenda);
		initNewAtividadeAgenda();
	}
	
	public void adicionarParticipanteExterno()
	{
		if(newAtividadeAgenda.getParticipanteExternoSelecionado().getId() != null)
		{
//			Contato participanteExterno = em.find(Contato.class, newAtividadeAgenda.getParticipanteExternoSelecionado().getId());
			Contato participanteExterno = contatoListProducer.findById(newAtividadeAgenda.getParticipanteExternoSelecionado().getId());
			AtividadeAgendaParticipantesExternos atividadeAgendaParticipantesExternos = new AtividadeAgendaParticipantesExternos();
			atividadeAgendaParticipantesExternos.setParticipanteExterno(participanteExterno);
			newAtividadeAgenda.getParticipantesExternos().add(atividadeAgendaParticipantesExternos);
		}
	}
	public void adicionarParticipanteInterno()
	{
		if(newAtividadeAgenda.getParticipanteInternoSelecionado().getId() != null)
		{
			Principals participanteInterno = principalsListProducer.findById(newAtividadeAgenda.getParticipanteInternoSelecionado().getId());
			AtividadeAgendaParticipantesInternos atividadeAgendaParticipantesInternos = new AtividadeAgendaParticipantesInternos();
			atividadeAgendaParticipantesInternos.setParticipantesInternos(participanteInterno);
			newAtividadeAgenda.getParticipantesInternos().add(atividadeAgendaParticipantesInternos);
		}
	}

	@PostConstruct
	public void initNewAtividadeAgenda() {
		newAtividadeAgenda = new AtividadeAgenda();
	}
}
