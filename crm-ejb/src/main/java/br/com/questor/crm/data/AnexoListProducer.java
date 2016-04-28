package br.com.questor.crm.data;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jfree.util.Log;

import br.com.questor.crm.model.Anexo;
import br.com.questor.crm.model.Lead;

@RequestScoped
public class AnexoListProducer {
	@Inject
	private EntityManager em;
	
	private List<Anexo> anexos;
	
	@Produces
	@Named
	public List<Anexo> getAnexos()
	{
		return anexos;
	}
	
	public void onAnexoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Anexo anexo) {
//		retrieveAllAnexosOrderedByDescricao();
	}
	
	public void retrieveAllAnexosOrderedByDescricao() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Anexo> criteria = cb.createQuery(Anexo.class);
//		Root<Anexo> anexo = criteria.from(Anexo.class);
//		criteria.select(anexo).orderBy(cb.asc(anexo.get("descricao")));
//		anexos = em.createQuery(criteria).getResultList();
		anexos = em.createNamedQuery("Anexo.findAll").getResultList();
	}
	
	public List<Anexo> retrieveAllAnexosByLeadOrderedByDescricao(Lead lead)
	{
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Anexo> criteria = cb.createQuery(Anexo.class);
//		Root<Anexo> anexo = criteria.from(Anexo.class);
//		criteria.select(anexo).where(cb.equal(anexo.get("lead"), lead)).orderBy(cb.asc(anexo.get("descricao")));
//		return em.createQuery(criteria).getResultList();
		try
		{
			return em.createNamedQuery("Anexo.findByLead").setParameter("leadId", lead.getId()).getResultList();
		}
		catch(Exception e)
		{
			Log.error("StreamCorruptedException");
			return new ArrayList<Anexo>();
		}
	}
}
