package br.com.questor.crm.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.questor.crm.model.Anexo;

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
		retrieveAllAnexosOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllAnexosOrderedByDescricao() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Anexo> criteria = cb.createQuery(Anexo.class);
		Root<Anexo> anexo = criteria.from(Anexo.class);
		criteria.select(anexo).orderBy(cb.asc(anexo.get("descricao")));
		anexos = em.createQuery(criteria).getResultList();
	}
}
