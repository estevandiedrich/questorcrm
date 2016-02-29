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

import br.com.questor.crm.model.TipoServico;

@RequestScoped
public class TiposServicoListProducer {
	@Inject
	private EntityManager em;
	
	private List<TipoServico> tiposServico;
	
	@Produces
	@Named
	public List<TipoServico> getTiposServico()
	{
		return tiposServico;
	}
	
	public void onTipoServicoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final TipoServico tipoProduto) {
		retrieveAllTipoServicosOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllTipoServicosOrderedByDescricao() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TipoServico> criteria = cb.createQuery(TipoServico.class);
		Root<TipoServico> tipoServico = criteria.from(TipoServico.class);
		criteria.select(tipoServico).orderBy(cb.asc(tipoServico.get("descricao")));
		tiposServico = em.createQuery(criteria).getResultList();
	}
}