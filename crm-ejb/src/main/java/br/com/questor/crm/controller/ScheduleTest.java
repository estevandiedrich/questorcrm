package br.com.questor.crm.controller;

import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.primefaces.context.RequestContext;

@Singleton
@Startup
public class ScheduleTest {
	@Inject
    private Logger log;
	@Inject
	private EntityManager em;
	
//	@Schedule(second = "*",minute="*/10",hour="*")
//	public void buscaAtividadesAgenda()
//	{
//		log.info("teste2");
//		if(RequestContext.getCurrentInstance()!=null)
//		{
//			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message Title", "Message body");
//			RequestContext.getCurrentInstance().showMessageInDialog(message);
//		}
//	}
}
