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

import br.com.questor.crm.model.Cidade;
import br.com.questor.crm.model.UF;

@RequestScoped
public class CidadeListProducer {
	@Inject
	private EntityManager em;
	
	private List<Cidade> cidades;
	
	@Produces
	@Named
	public List<Cidade> getCidades()
	{
		return cidades;
	}
	
	public void onCidadeListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Cidade cidade) {
		retrieveAllCidadesOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllCidadesOrderedByNome() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Cidade> criteria = cb.createQuery(Cidade.class);
//		Root<Cidade> cliente = criteria.from(Cidade.class);
//		criteria.select(cliente).orderBy(cb.asc(cliente.get("nome")));
//		cidades = em.createQuery(criteria).getResultList();
		cidades = em.createNamedQuery("Cidade.findAll").getResultList();
	}
	
	public List<Cidade> retrieveAllCidadesByUfOrderedByNome(UF uf)
	{
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Cidade> criteria = cb.createQuery(Cidade.class);
//		Root<Cidade> cidade = criteria.from(Cidade.class);
//		criteria.select(cidade).where(cb.equal(cidade.get("uf"), uf)).orderBy(cb.asc(cidade.get("nome")));
//		return em.createQuery(criteria).getResultList();
		@SuppressWarnings(value = "unchecked")
		List<Cidade> cidades = em.createNamedQuery("Cidade.findByUF").setParameter("uf", uf.getId()).getResultList();
		return cidades;
	}
}
