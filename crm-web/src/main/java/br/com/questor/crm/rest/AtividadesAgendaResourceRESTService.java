package br.com.questor.crm.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.com.questor.crm.controller.SalvarAtividadeAgenda;

@Path("/atividadesagenda")
@RequestScoped
public class AtividadesAgendaResourceRESTService {
	@Inject
	private SalvarAtividadeAgenda salvarAtivadadeAgenda;
	@GET
	@Produces("application/json")
	public long[] getDataEHoraAtividadesAgenda() {
	   return salvarAtivadadeAgenda.getDataEHoraAtividadesAgenda();
	}
}
