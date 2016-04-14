package br.com.questor.crm.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.questor.crm.enums.LeadEnum;
import br.com.questor.crm.model.Lead;

@Singleton
@Startup
public class AlteraStatusLeadSchedule {
	@Inject
    private Logger log;
	@Inject
	private EntityManager em;
	
	@Schedule(second = "59",minute="59",hour="23")
	public void alteraStatusLeadSchedule()
	{
		log.info(AlteraStatusLeadSchedule.class.getName());
		List<Lead> leadsEmAvaliacao = em.createNamedQuery("Lead.findLeadsEmAvaliacao").getResultList();
		for(Lead lead:leadsEmAvaliacao)
		{
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(lead.getDataCadastro());
			gc.add(Calendar.DAY_OF_MONTH, Integer.parseInt(lead.getUf().getPrazo()));
			if(gc.after(new Date()))
			{
				lead.setStatusLead(LeadEnum.DESQUALIFICADA);
				em.merge(lead);
			}
		}
	}
}
