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
import br.com.questor.crm.data.ModuloSelecionadoListProducer;
import br.com.questor.crm.model.Modulo;
import br.com.questor.crm.model.ModuloSelecionado;
import br.com.questor.crm.model.ProdutoModulosSelecionados;
import br.com.questor.crm.model.Proposta;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarProposta {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Proposta> propostaEventSrc;
	
	@Inject
	private ModuloSelecionadoListProducer moduloSelecionadoListProducer;
	
	@Inject
	private ModuloListProducer moduloListProducer;
	
	private Proposta newProposta;
	
	@Produces
	@Named
	public Proposta getNewProposta() {
		return newProposta;
	}
	public void setNewProposta(Proposta proposta)
	{
		newProposta = proposta;
	}
	public void setProdutoModulosSelecionados(List<ProdutoModulosSelecionados> produtosModulosSelecionados)
	{
		newProposta.setProdutosModulosSelecionados(produtosModulosSelecionados);
	}
	public void setProdutoModulosSelecionados(ProdutoModulosSelecionados produto)
	{
		newProposta.setProdutoModulosSelecionados(produto);
		List<ModuloSelecionado> modulosSelecionados = moduloSelecionadoListProducer.retrieveAllModuloSelecionadoByProdutoModulosSelecionados(produto);
		List<Modulo> modulos = moduloListProducer.retrieveAllModulosByProdutoOrderedByNome(produto.getProduto());
		produto.getProduto().setModulos(modulos);
		produto.setModulosSelecionados(modulosSelecionados);
	}
	public String novo() {
		initNewProposta();
		return "/pages/protected/user/proposta?faces-redirect=true";
	}
//	public void adicionarCotacao(Conta conta)
//	{
//		conta.getCotacoes().add(newProposta);
//		newProposta.setLead(conta);
//		em.persist(newProposta);
//	}
//	public void adicionar(String id)
//	{
//		Produto produto = em.find(Produto.class, Long.parseLong(id));
////		ProdutoCotacao produtoCotacao = new ProdutoCotacao();
////		produtoCotacao.setProduto(produto);
////		produtoCotacao.setCotacao(newProposta);
//		if(newProposta.getValorInicial() == null)
//		{
//			newProposta.setValorInicial(BigDecimal.ZERO);
//		}
//		newProposta.getProdutos().add(produto);
//	}
	
	public void salvar() throws Exception {
		log.info("Salvando Proposta" + newProposta.getDescricao());
		em.persist(newProposta);
		for(ProdutoModulosSelecionados p:newProposta.getProdutosModulosSelecionados())
		{
			p.setProposta(newProposta);
			em.persist(p);
			for(ModuloSelecionado m:p.getModulosSelecionados())
			{
				m.setProdutosModulosSelecionados(p);
				em.persist(m);
			}
		}
		propostaEventSrc.fire(newProposta);
		initNewProposta();
	}

	@PostConstruct
	public void initNewProposta() {
		newProposta = new Proposta();
	}
}