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
		modulos = em.createNamedQuery("Modulo.findAll").getResultList();
	}
	
	public List<Modulo> retrieveAllModulosByProdutoOrderedByNome(Produto produto) {
		return em.createNamedQuery("Modulo.findByProduto").setParameter("produto", produto.getId()).getResultList();
	}
}
