package br.com.questor.crm.controller;

import java.math.BigDecimal;
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
import br.com.questor.crm.data.ProdutoModulosSelecionadosListProducer;
import br.com.questor.crm.enums.TipoProdutoEnum;
import br.com.questor.crm.model.Cotacao;
import br.com.questor.crm.model.Modulo;
import br.com.questor.crm.model.ModuloSelecionado;
import br.com.questor.crm.model.Oportunidade;
import br.com.questor.crm.model.ProdutoModulosSelecionados;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarCotacao {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Cotacao> cotacaoEventSrc;
	
	@Inject
	private ModuloSelecionadoListProducer moduloSelecionadoListProducer;
	
	@Inject
	private ProdutoModulosSelecionadosListProducer produtoModulosSelecionadosListProducer;
	
	@Inject
	private ModuloListProducer moduloListProducer;
	
	private Cotacao newCotacao;
	
	@Produces
	@Named
	public Cotacao getNewCotacao() {
		return newCotacao;
	}
	public void setNewCotacao(Cotacao cotacao)
	{
		newCotacao = cotacao;
	}
	public void setProdutoModulosSelecionados(Cotacao cotacao)
	{
		List<ProdutoModulosSelecionados> produtosModulosSelecionados = produtoModulosSelecionadosListProducer.retrieveAllProdutoModulosSelecionadosByPropostaOrderedByDescricao(cotacao);
		newCotacao.setProdutosModulosSelecionados(produtosModulosSelecionados);
	}
	public void setProdutoModulosSelecionados(ProdutoModulosSelecionados produto)
	{
		if(newCotacao.getId() != null)
		{
			newCotacao.setProdutoModulosSelecionados(produto);
			List<ModuloSelecionado> modulosSelecionados = moduloSelecionadoListProducer.retrieveAllModuloSelecionadoByProdutoModulosSelecionados(produto);
			List<Modulo> modulos = moduloListProducer.retrieveAllModulosByProdutoOrderedByNome(produto.getProduto());
			produto.getProduto().setModulos(modulos);
			produto.setModulosSelecionados(modulosSelecionados);
		}
	}
	public String novo() {
		initNewCotacao();
		return "/pages/protected/user/cotacao?faces-redirect=true";
	}
	public void adicionarCotacao(Oportunidade oportunidade)
	{
		oportunidade.setValorTotalProdutos(BigDecimal.ZERO);
		oportunidade.setValorTotalServicos(BigDecimal.ZERO);
		for(ProdutoModulosSelecionados p:newCotacao.getProdutosModulosSelecionados())
		{
			if(p.getProduto().getTipoProduto() == TipoProdutoEnum.MANUTENCAO)
			{
				oportunidade.setValorTotalProdutos(oportunidade.getValorTotalProdutos().add(p.getValorTotal()));
			}
			else
			{
				oportunidade.setValorTotalServicos(oportunidade.getValorTotalServicos().add(p.getValorTotal()));
			}
		}
		oportunidade.getCotacoes().add(newCotacao);
		initNewCotacao();
	}
	
	public void salvar() throws Exception {
		log.info("Salvando Cotacao" + newCotacao.getDescricao());
		em.persist(newCotacao);
		for(ProdutoModulosSelecionados p:newCotacao.getProdutosModulosSelecionados())
		{
			p.setProposta(newCotacao);
			em.persist(p);
			for(ModuloSelecionado m:p.getModulosSelecionados())
			{
				m.setProdutosModulosSelecionados(p);
				em.persist(m);
			}
		}
		cotacaoEventSrc.fire(newCotacao);
		initNewCotacao();
	}

	@PostConstruct
	public void initNewCotacao() {
		newCotacao = new Cotacao();
	}
}