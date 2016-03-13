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

import br.com.questor.crm.model.Proposta;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.ModuloSelecionado;
import br.com.questor.crm.model.ProdutoModulosSelecionados;

@RequestScoped
public class PropostaListProducer {
	@Inject
	private EntityManager em;
	
	private List<Proposta> propostas;
	
	@Inject
	private ProdutoModulosSelecionadosListProducer produtoModulosSelecionadosListProducer;
	
	@Inject
	private ModuloSelecionadoListProducer modulosSelecionadosListProducer;
	
	@Produces
	@Named
	public List<Proposta> getPropostas()
	{
		return propostas;
	}
	
	public void onCotacaoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Proposta proposta) {
		retrieveAllCotacoesOrderedByDescricao();
	}
	@PostConstruct
	public void retrieveAllCotacoesOrderedByDescricao() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Proposta> criteria = cb.createQuery(Proposta.class);
		Root<Proposta> proposta = criteria.from(Proposta.class);
		criteria.select(proposta).orderBy(cb.asc(proposta.get("descricao")));
		propostas = em.createQuery(criteria).getResultList();
		for(Proposta p:propostas)
		{
			List<ProdutoModulosSelecionados> produtosModulosSelecionados = produtoModulosSelecionadosListProducer.retrieveAllProdutoModulosSelecionadosByPropostaOrderedByDescricao(p);
			for(ProdutoModulosSelecionados produtoModulosSelecionados:produtosModulosSelecionados)
			{
				List<ModuloSelecionado> modulosSelecionados = modulosSelecionadosListProducer.retrieveAllModuloSelecionadoByProdutoModulosSelecionados(produtoModulosSelecionados);
				produtoModulosSelecionados.setModulosSelecionados(modulosSelecionados);
			}
			p.setProdutoModulosSelecionados(produtosModulosSelecionados);
		}
	}
	public List<Proposta> retrieveAllCotacoesByLeadOrderedByDataEHoraCriacao(Lead lead) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Proposta> criteria = cb.createQuery(Proposta.class);
		Root<Proposta> proposta = criteria.from(Proposta.class);
		criteria.select(proposta).where(cb.equal(proposta.get("lead"), lead)).orderBy(cb.asc(proposta.get("dataEHoraCriacao")));
//		criteria.select(proposta).orderBy(cb.asc(proposta.get("descricao")));
		propostas = em.createQuery(criteria).getResultList();
		return propostas;
	}
}
