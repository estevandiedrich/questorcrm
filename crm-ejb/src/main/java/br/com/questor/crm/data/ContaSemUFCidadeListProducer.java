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
import br.com.questor.crm.model.Principals;

@RequestScoped
public class ContaSemUFCidadeListProducer {
	@Inject
	private EntityManager em;
	
	@Inject
	private LoginBean loginBean;
	
	@Inject
	private GrupoUsuariosPrincipalsListProducer grupoUsuariosPrincipalsListProducer;
	
	@Inject
	private GrupoUsuariosLeadListProducer grupoUsuariosLeadListProducer;
	
	private List<Lead> contasSemUFCidade;
	
	@Produces
	@Named
	public List<Lead> getContasSemUFCidade()
	{
		return contasSemUFCidade;
	}
	public void onLeadListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Lead lead) {
		retrieveAllLeadsOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllLeadsOrderedByNome() {
		Principals l = this.loginBean.getPrincipalsFromDB();
		if(loginBean.isCallerInRole("ADMIN"))
		{
			contasSemUFCidade = em.createNamedQuery("Conta.findAllSemUFCidade").getResultList();
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
				contasSemUFCidade = em.createNamedQuery("Conta.findByIdsSemUFCidade").setParameter("leads", leadsId).getResultList();
			}
			else
			{
				contasSemUFCidade = new ArrayList<>();
			}
		}
	}
}
