package br.com.questor.crm.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.data.ModuloListProducer;
import br.com.questor.crm.model.Modulo;
import br.com.questor.crm.model.ModuloSelecionado;
import br.com.questor.crm.model.Produto;
import br.com.questor.crm.model.ProdutoModulosSelecionados;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarProdutoModulosSelecionados {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private ModuloListProducer moduloListProducer;
	
	@Inject
	private Event<ProdutoModulosSelecionados> produtoModulosSelecionadosEventSrc;
	
	private ProdutoModulosSelecionados newProdutoModulosSelecionados;
	
	@Produces
	@Named
	public ProdutoModulosSelecionados getNewProdutoModulosSelecionados() {
		return newProdutoModulosSelecionados;
	}
	
	public void setProduto() {
        Long id = newProdutoModulosSelecionados.getProduto().getId();
        Produto p =em.find(Produto.class, id);
        List<Modulo> modulos = moduloListProducer.retrieveAllModulosByProdutoOrderedByNome(p);
        p.setModulos(modulos);
        newProdutoModulosSelecionados.setProduto(p);
    }
	public void adicionarModulo(Long id)
	{
		Modulo modulo = em.find(Modulo.class, id);
		ModuloSelecionado moduloSelecionado = new ModuloSelecionado();
		moduloSelecionado.setModulo(modulo);
//		moduloSelecionado.setProdutosModulosSelecionados(newProdutoModulosSelecionados);
		newProdutoModulosSelecionados.getModulosSelecionados().add(moduloSelecionado);
	}
	
	public void salvar() throws Exception {
		log.info("Salvando " + newProdutoModulosSelecionados.getId());
		em.persist(newProdutoModulosSelecionados);
		for(ModuloSelecionado moduloSelecionado:newProdutoModulosSelecionados.getModulosSelecionados())
		{
			moduloSelecionado.setProdutosModulosSelecionados(newProdutoModulosSelecionados);
			em.persist(moduloSelecionado);
		}
		produtoModulosSelecionadosEventSrc.fire(newProdutoModulosSelecionados);
		initNewProdutoModulosSelecionados();
	}

	@PostConstruct
	public void initNewProdutoModulosSelecionados() {
		newProdutoModulosSelecionados = new ProdutoModulosSelecionados();
	}
}