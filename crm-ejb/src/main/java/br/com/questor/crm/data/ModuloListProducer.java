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

import br.com.questor.crm.model.Modulo;
import br.com.questor.crm.model.Produto;

@RequestScoped
public class ModuloListProducer {
	@Inject
	private EntityManager em;
	
	private List<Modulo> modulos;
	
	@Produces
	@Named
	public List<Modulo> getModulos()
	{
		return modulos;
	}
	
	public void onModuloListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Modulo modulo) {
		retrieveAllModulosOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllModulosOrderedByNome() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Modulo> criteria = cb.createQuery(Modulo.class);
//		Root<Modulo> modulo = criteria.from(Modulo.class);
//		criteria.select(modulo).orderBy(cb.asc(modulo.get("descricao")));
//		modulos = em.createQuery(criteria).getResultList();
//		for(Modulo m:modulos)
//		{
//			if(m.getProduto() != null)
//			{
//				Produto p = em.find(Produto.class, m.getProduto().getId());
//				m.setProduto(p);
//			}
//		}
		modulos = em.createNamedQuery("Modulo.findAll").getResultList();
	}
	
	public List<Modulo> retrieveAllModulosByProdutoOrderedByNome(Produto produto) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Modulo> criteria = cb.createQuery(Modulo.class);
//		Root<Modulo> modulo = criteria.from(Modulo.class);
//		criteria.select(modulo).where(cb.equal(modulo.get("produto"), produto)).orderBy(cb.asc(modulo.get("descricao")));
//		modulos = em.createQuery(criteria).getResultList();
//		return modulos;
		return em.createNamedQuery("Modulo.findByProduto").setParameter("produto", produto.getId()).getResultList();
	}
}
