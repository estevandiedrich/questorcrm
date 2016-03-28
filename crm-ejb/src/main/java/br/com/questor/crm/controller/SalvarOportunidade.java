package br.com.questor.crm.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.questor.crm.data.ContatoListProducer;
import br.com.questor.crm.data.CotacaoListProducer;
import br.com.questor.crm.data.OportunidadeContatoListProducer;
import br.com.questor.crm.model.Contato;
import br.com.questor.crm.model.Cotacao;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.ModuloSelecionado;
import br.com.questor.crm.model.Oportunidade;
import br.com.questor.crm.model.OportunidadeContato;
import br.com.questor.crm.model.ProdutoModulosSelecionados;

@Stateful
//@Model
@Named
@SessionScoped
public class SalvarOportunidade {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	
	@Inject
	private Event<Oportunidade> oportunidadeEventSrc;
	
	@Inject
	private ContatoListProducer contatoListProducer;
	
	@Inject
	private CotacaoListProducer cotacaoListProducer;
	
	@Inject
	private OportunidadeContatoListProducer oportunidadeContatoListProducer;
	
	private Oportunidade newOportunidade;
	
	@Produces
	@Named
	public Oportunidade getNewOportunidade() {
		return newOportunidade;
	}
	public String novo() {
		initNewOportunidade();
		return "/pages/protected/user/oportunidade?faces-redirect=true";
	}
	public void adicionarContato()
	{
		if(newOportunidade.getContatoSelecionado() != null && newOportunidade.getContatoSelecionado().getId() != null)
		{
//			Contato contato = em.find(Contato.class, newOportunidade.getContatoSelecionado().getId());
			Contato contato = contatoListProducer.findById(newOportunidade.getContatoSelecionado().getId());
			OportunidadeContato oportunidadeContato = new OportunidadeContato();
			oportunidadeContato.setContato(contato);
			newOportunidade.getContatos().add(oportunidadeContato);
		}
	}
	public void salvar() throws Exception {
		log.info("Salvando Oportunidade" + newOportunidade.getDescricao());
		em.persist(newOportunidade);
		for(OportunidadeContato oportunidadeContato:newOportunidade.getContatos())
		{
			oportunidadeContato.setOportunidade(newOportunidade);
			em.persist(oportunidadeContato);
		}
		for(Cotacao cotacao:newOportunidade.getCotacoes())
		{
			cotacao.setOportunidade(newOportunidade);
			em.persist(cotacao);
			for(ProdutoModulosSelecionados p:cotacao.getProdutosModulosSelecionados())
			{
				p.setProposta(cotacao);
				em.persist(p);
				for(ModuloSelecionado m:p.getModulosSelecionados())
				{
					m.setProdutosModulosSelecionados(p);
					em.persist(m);
				}
			}
		}
		oportunidadeEventSrc.fire(newOportunidade);
		initNewOportunidade();
	}
	public void setOportunidade(Oportunidade oportunidade)
	{
		newOportunidade = oportunidade;
		List<Cotacao> cotacoes = cotacaoListProducer.retrieveAllCotacoesByOportunidadeOrderedByDataCriacao(newOportunidade);
		List<OportunidadeContato> oportunidadeContatos = oportunidadeContatoListProducer.retrieveAllOportunidadeContatosByOportunidade(newOportunidade);
		List<Contato> contatos = contatoListProducer.retrieveAllContatosByLeadOrderedByNome(newOportunidade.getConta());
		newOportunidade.getConta().setContatos(contatos);
		newOportunidade.setContatos(oportunidadeContatos);
		newOportunidade.setCotacoes(cotacoes);
	}
	public void setConta(AjaxBehaviorEvent e)
	{
		if(newOportunidade.getConta() != null && newOportunidade.getConta().getId() != null)
		{
			Lead conta = em.find(Lead.class, newOportunidade.getConta().getId());
			conta.setContatos(contatoListProducer.retrieveAllContatosByLeadOrderedByNome(conta));
			newOportunidade.setConta(conta);
		}
	}
	@PostConstruct
	public void initNewOportunidade() {
		newOportunidade = new Oportunidade();
	}
}
