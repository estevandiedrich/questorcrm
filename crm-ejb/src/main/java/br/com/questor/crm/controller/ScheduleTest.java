package br.com.questor.crm.controller;

import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Singleton
@Startup
public class ScheduleTest {
	@Inject
    private Logger log;
	@Inject
	private EntityManager em;
	
	@Schedule(second = "*",minute="*/1",hour="*")
	public void buscaAtividadesAgenda()
	{
		log.info("teste2");
	}
}
