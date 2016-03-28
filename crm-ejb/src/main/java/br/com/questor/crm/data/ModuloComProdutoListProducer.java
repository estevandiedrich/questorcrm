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

@RequestScoped
public class ModuloComProdutoListProducer {
	@Inject
	private EntityManager em;
	
	private List<Modulo> modulosComProduto;
	
	@Produces
	@Named
	public List<Modulo> getModulosComProduto()
	{
		return modulosComProduto;
	}
	
	public void onModuloListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Modulo modulo) {
		retrieveAllModulosOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllModulosOrderedByNome() {
		modulosComProduto = em.createNamedQuery("Modulo.findAllComProduto").getResultList();
	}
}
