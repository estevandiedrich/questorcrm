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

import br.com.questor.crm.model.ModuloSelecionado;
import br.com.questor.crm.model.ProdutoModulosSelecionados;

@RequestScoped
public class ModuloSelecionadoListProducer {
	@Inject
	private EntityManager em;
	
	private List<ModuloSelecionado> modulosSelecionados;
	
	@Produces
	@Named
	public List<ModuloSelecionado> getModuloSelecionados()
	{
		return modulosSelecionados;
	}
	
	public void onModuloSelecionadoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final ModuloSelecionado moduloSelecionado) {
		retrieveAllModuloSelecionados();
	}
	@PostConstruct
	public void retrieveAllModuloSelecionados() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ModuloSelecionado> criteria = cb.createQuery(ModuloSelecionado.class);
		Root<ModuloSelecionado> moduloSelecionado = criteria.from(ModuloSelecionado.class);
		criteria.select(moduloSelecionado);
		modulosSelecionados = em.createQuery(criteria).getResultList();
	}
	
	public List<ModuloSelecionado> retrieveAllModuloSelecionadoByProdutoModulosSelecionados(ProdutoModulosSelecionados produtoModulosSelecionados)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ModuloSelecionado> criteria = cb.createQuery(ModuloSelecionado.class);
		Root<ModuloSelecionado> moduloSelecionado = criteria.from(ModuloSelecionado.class);
		criteria.select(moduloSelecionado).where(cb.equal(moduloSelecionado.get("produtoModulosSelecionados"), produtoModulosSelecionados));
		return em.createQuery(criteria).getResultList();
	}
}
