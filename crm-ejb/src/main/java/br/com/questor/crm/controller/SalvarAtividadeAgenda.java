package br.com.questor.crm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.data.AtividadeAgendaListProducer;
import br.com.questor.crm.data.ContatoListProducer;
import br.com.questor.crm.model.AtividadeAgenda;
import br.com.questor.crm.model.Contato;
import br.com.questor.crm.model.Lead;

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
	
	public void setLead(Lead lead)
	{
		leadSelecionada = lead;
		List<Contato> contatos = contatoListProducer.retrieveAllContatosByLeadOrderedByNome(lead);
		if(contatos == null)
			contatos = new ArrayList<Contato>();
		lead.setContatos(contatos);
		List<AtividadeAgenda> atividadesAgenda = atividadeAgendaListProducer.retrieveAllAtividadeAgendasByLeadOrderedByDataEHora(lead);
		lead.setAtividadesAgenda(atividadesAgenda);
		newAtividadeAgenda.setLead(lead);
	}
	
	@Produces
	@Named
	public AtividadeAgenda getNewAtividadeAgenda() {
		return newAtividadeAgenda;
	}
	
	public void salvar() throws Exception {
		log.info("Salvando AtividadeAgenda" + newAtividadeAgenda.getLembrete());
		String participantes = Arrays.toString(newAtividadeAgenda.getParticipantesSelecionados()).replace("[", "").replace("]", "");
		newAtividadeAgenda.setParticipantes(participantes);
		em.persist(newAtividadeAgenda);
		em.merge(newAtividadeAgenda.getLead());
		atividadeAgendaEventSrc.fire(newAtividadeAgenda);
		initNewAtividadeAgenda();
	}

	@PostConstruct
	public void initNewAtividadeAgenda() {
		newAtividadeAgenda = new AtividadeAgenda();
	}
}
