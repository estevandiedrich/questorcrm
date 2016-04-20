package br.com.questor.crm.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import br.com.questor.crm.model.AtividadeAgenda;
import br.com.questor.crm.model.Principals;
import io.undertow.util.DateUtils;

@Stateful
//@Model
@SessionScoped
@Named
public class LoginBean {
	
	@Inject
    private Logger log;
	@Inject
	private EntityManager em;
	@Inject
	private SalvarPrincipals salvarPrincipals;
	
	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String login() throws Exception
	{
		log.info("logando "+username);
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try {
			request.login(this.username, this.password);
			log.info("Is caller in role ADMIN "+request.isUserInRole("ADMIN"));
		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage("Login failed."));
			return "/pages/public/error?faces-redirect=true";
		}
		Principals principal2 = this.getPrincipalsFromDB();
		if(principal2.isPrimeiroLogin())
		{
			return salvarPrincipals.primeiroAcesso(principal2);
		}
		else
		{
			return "/pages/protected/user/index?faces-redirect=true";
		}
	}
	public Principals getPrincipalsFromDB()
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Principals> criteria = cb.createQuery(Principals.class);
		Root<Principals> principal = criteria.from(Principals.class);
		criteria.select(principal).where(cb.equal(principal.get("principalId"), username));
		Principals principal2 = em.createQuery(criteria).getSingleResult();
		return principal2;
	}
	public boolean isCallerInRole(String role)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		log.info("Is caller in role "+role+" "+request.isUserInRole(role));
//		return ctx.isCallerInRole(role);
		return request.isUserInRole(role);
	}
	public String logout() throws Exception
	{
		log.info("logout ");
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try {
			request.logout();
			context.getExternalContext().invalidateSession();
		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage("Logout failed."));
			
		}
		return "/pages/public/login?faces-redirect=true";
	}
	public void verificaAtividadeAgenda()
	{
		log.info("Buscando atividade agenda para o usuario "+getPrincipalsFromDB().getNome());
		List<AtividadeAgenda> aa = em.createNamedQuery("AtividadeAgenda.findByPrincipalEData").setParameter("principal", getPrincipalsFromDB().getId()).setParameter("dataInicial", new Date(),TemporalType.DATE).setParameter("dataFinal", new Date(),TemporalType.TIMESTAMP).getResultList();
		if(aa != null && aa.size() > 0)
		{
			for(AtividadeAgenda a:aa)
			{
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(a.getDataEHora());
				if(a.isAvisarComAntecedencia())
				{
					gc.add(Calendar.MINUTE, Integer.parseInt(a.getAntecedencia())*-1);
				}
				if((new Date()).after(gc.getTime()))
				{
					RequestContext.getCurrentInstance().execute("PF('myDialogVar').show();");
					a.setAvisado(true);
					em.merge(a);
					break;
				}
			}
		}
	}
}
