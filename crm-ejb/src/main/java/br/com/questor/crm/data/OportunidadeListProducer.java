package br.com.questor.crm.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.controller.LoginBean;
import br.com.questor.crm.model.GrupoUsuariosLead;
import br.com.questor.crm.model.GrupoUsuariosPrincipals;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.Oportunidade;
import br.com.questor.crm.model.Principals;

@RequestScoped
public class OportunidadeListProducer {
	@Inject
	private EntityManager em;
	@Inject
	private LoginBean loginBean;
	@Inject
	private GrupoUsuariosPrincipalsListProducer grupoUsuariosPrincipalsListProducer;
	@Inject
	private GrupoUsuariosLeadListProducer grupoUsuariosLeadListProducer;
	
	private List<Oportunidade> oportunidades;
	
	@Produces
	@Named
	public List<Oportunidade> getOportunidades()
	{
		return oportunidades;
	}
	
	public void onOportunidadeListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Oportunidade oportunidade) {
		retrieveAllOportunidadesOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllOportunidadesOrderedByNome() {
//		@SuppressWarnings(value = "unchecked")
		Principals l = this.loginBean.getPrincipalsFromDB();
		if(loginBean.isCallerInRole("ADMIN"))
		{
			oportunidades = em.createNamedQuery("Oportunidade.findAll").getResultList();
		}
		else
		{
			List<GrupoUsuariosPrincipals> grupoUsuariosPrincipals = grupoUsuariosPrincipalsListProducer.retrieveAllGrupoUsuariosPrincipalsByPrincipal(l);
			l.setGruposUsuarios(grupoUsuariosPrincipals);
			List<GrupoUsuariosLead> grupoUsuariosLeads2 = new ArrayList<GrupoUsuariosLead>();
			List<Long> leadsId = new ArrayList<Long>();
			for(GrupoUsuariosPrincipals g:l.getGruposUsuarios())
			{
				List<GrupoUsuariosLead> grupoUsuariosLeads = grupoUsuariosLeadListProducer.retrieveAllGrupoUsuariosLeadByGrupoUsuarios(g.getGrupoUsuarios());
				grupoUsuariosLeads2.addAll(grupoUsuariosLeads);
			}
			for(GrupoUsuariosLead g:grupoUsuariosLeads2)
			{
				leadsId.add(g.getLead().getId());
			}
			if(leadsId.size() > 0)
			{
				oportunidades = em.createNamedQuery("Oportunidade.findByContaIds").setParameter("contas", leadsId).getResultList();
			}
			else
			{
				oportunidades = new ArrayList<>();
			}
		}
	}
}
