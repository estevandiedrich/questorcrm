package br.com.questor.crm.data;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.questor.crm.model.Cotacao;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.ModuloSelecionado;
import br.com.questor.crm.model.Oportunidade;
import br.com.questor.crm.model.ProdutoModulosSelecionados;

@RequestScoped
public class CotacaoListProducer {
	@Inject
	private EntityManager em;
	
	private List<Cotacao> cotacoes;
	
	@Inject
	private ProdutoModulosSelecionadosListProducer produtoModulosSelecionadosListProducer;
	
	@Inject
	private ModuloSelecionadoListProducer modulosSelecionadosListProducer;
	
	@Produces
	@Named
	public List<Cotacao> getCotacoes()
	{
		return cotacoes;
	}
	
//	public void onCotacaoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Cotacao cotacao) {
//		retrieveAllCotacoesOrderedByDescricao();
//	}
//	@PostConstruct
	public void retrieveAllCotacoesOrderedByDescricao() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Cotacao> criteria = cb.createQuery(Cotacao.class);
//		Root<Cotacao> cotacao = criteria.from(Cotacao.class);
//		criteria.select(cotacao).orderBy(cb.asc(cotacao.get("descricao")));
//		cotacoes = em.createQuery(criteria).getResultList();
		cotacoes = em.createNamedQuery("Cotacao.findAll").getResultList();
		for(Cotacao p:cotacoes)
		{
			List<ProdutoModulosSelecionados> produtosModulosSelecionados = produtoModulosSelecionadosListProducer.retrieveAllProdutoModulosSelecionadosByPropostaOrderedByDescricao(p);
			for(ProdutoModulosSelecionados produtoModulosSelecionados:produtosModulosSelecionados)
			{
				List<ModuloSelecionado> modulosSelecionados = modulosSelecionadosListProducer.retrieveAllModuloSelecionadoByProdutoModulosSelecionados(produtoModulosSelecionados);
				produtoModulosSelecionados.setModulosSelecionados(modulosSelecionados);
			}
			p.setProdutosModulosSelecionados(produtosModulosSelecionados);
		}
	}
	public List<Cotacao> retrieveAllCotacoesByLeadOrderedByDataEHoraCriacao(Lead lead) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cotacao> criteria = cb.createQuery(Cotacao.class);
		Root<Cotacao> cotacao = criteria.from(Cotacao.class);
		criteria.select(cotacao).where(cb.equal(cotacao.get("lead"), lead)).orderBy(cb.asc(cotacao.get("dataEHoraCriacao")));
//		criteria.select(proposta).orderBy(cb.asc(proposta.get("descricao")));
		List<Cotacao> cotacoes = em.createQuery(criteria).getResultList();
		return cotacoes;
	}
	public List<Cotacao> retrieveAllCotacoesByOportunidadeOrderedByDataCriacao(Oportunidade oportunidade)
	{
		List<Cotacao> cotacoes = new ArrayList<>();
		if(oportunidade.getId() != null)
		{
			cotacoes = em.createNamedQuery("Cotacao.findByOportunidade").setParameter("oportunidade", oportunidade.getId()).getResultList();
			for(Cotacao p:cotacoes)
			{
				List<ProdutoModulosSelecionados> produtosModulosSelecionados = produtoModulosSelecionadosListProducer.retrieveAllProdutoModulosSelecionadosByPropostaOrderedByDescricao(p);
				for(ProdutoModulosSelecionados produtoModulosSelecionados:produtosModulosSelecionados)
				{
					List<ModuloSelecionado> modulosSelecionados = modulosSelecionadosListProducer.retrieveAllModuloSelecionadoByProdutoModulosSelecionados(produtoModulosSelecionados);
					produtoModulosSelecionados.setModulosSelecionados(modulosSelecionados);
				}
				p.setProdutosModulosSelecionados(produtosModulosSelecionados);
			}
		}
		return cotacoes;
	}
}
