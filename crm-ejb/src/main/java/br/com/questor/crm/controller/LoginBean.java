package br.com.questor.crm.controller;

import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import br.com.questor.crm.model.Principals;

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
			return "/pages/public/error";
		}
		Principals principal2 = this.getPrincipalsFromDB();
		if(principal2.isPrimeiroLogin())
		{
			return salvarPrincipals.editar(principal2.getId().toString());
//			return "/pages/protected/user/principalsedit?faces-redirect=true&includeViewParams=true&id="+principal2.getId();
		}
		else
		{
			return "/pages/protected/user/index";
		}
	}
	public Principals getPrincipalsFromDB()
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Principals> criteria = cb.createQuery(Principals.class);
		Root<Principals> principal = criteria.from(Principals.class);
		criteria.select(principal).where(cb.equal(principal.get("PrincipalID"), username));
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
		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage("Logout failed."));
			
		}
		return "/pages/public/login";
	}
}