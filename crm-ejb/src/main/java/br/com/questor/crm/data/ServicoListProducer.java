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

import br.com.questor.crm.model.Servico;

@RequestScoped
public class ServicoListProducer {
	@Inject
	private EntityManager em;
	
	private List<Servico> servicos;
	
	@Produces
	@Named
	public List<Servico> getServicos()
	{
		return servicos;
	}
	
	public void onServicoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Servico servico) {
		retrieveAllServicosOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllServicosOrderedByDescricao() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Servico> criteria = cb.createQuery(Servico.class);
		Root<Servico> servico = criteria.from(Servico.class);
		criteria.select(servico).orderBy(cb.asc(servico.get("descricao")));
		servicos = em.createQuery(criteria).getResultList();
	}
}