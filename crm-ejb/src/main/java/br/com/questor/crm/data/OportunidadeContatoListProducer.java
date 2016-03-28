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

import br.com.questor.crm.model.Oportunidade;
import br.com.questor.crm.model.OportunidadeContato;

@RequestScoped
public class OportunidadeContatoListProducer {
	@Inject
	private EntityManager em;
	
	private List<OportunidadeContato> oportunidadeContatos;
	
	@Produces
	@Named
	public List<OportunidadeContato> getOportunidadeContatos()
	{
		return oportunidadeContatos;
	}
	
	public void onOportunidadeContatoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final OportunidadeContato oportunidade) {
		retrieveAllOportunidadeContatosOrderedByNome();
	}
	@PostConstruct
	public void retrieveAllOportunidadeContatosOrderedByNome() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<OportunidadeContato> criteria = cb.createQuery(OportunidadeContato.class);
//		Root<OportunidadeContato> cliente = criteria.from(OportunidadeContato.class);
//		criteria.select(cliente).orderBy(cb.asc(cliente.get("nome")));
//		oportunidadeContatos = em.createQuery(criteria).getResultList();
		oportunidadeContatos = em.createNamedQuery("OportunidadeContato.findAll").getResultList();
	}
	
	public List<OportunidadeContato> retrieveAllOportunidadeContatosByOportunidade(Oportunidade oportunidade)
	{
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<OportunidadeContato> criteria = cb.createQuery(OportunidadeContato.class);
//		Root<OportunidadeContato> cidade = criteria.from(OportunidadeContato.class);
//		criteria.select(cidade).where(cb.equal(cidade.get("uf"), uf)).orderBy(cb.asc(cidade.get("nome")));
//		return em.createQuery(criteria).getResultList();
		@SuppressWarnings(value = "unchecked")
		List<OportunidadeContato> oportunidadeContatos = em.createNamedQuery("OportunidadeContato.findByOportunidade").setParameter("oportunidade", oportunidade.getId()).getResultList();
		return oportunidadeContatos;
	}
}
