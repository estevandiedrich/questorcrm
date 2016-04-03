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

import br.com.questor.crm.model.Feed;

@RequestScoped
public class FeedListProducer {
	@Inject
	private EntityManager em;
	
	private List<Feed> feeds;
	
	@Produces
	@Named
	public List<Feed> getFeeds()
	{
		return feeds;
	}
	
	public void onFeedListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Feed feed) {
		retrieveAllFeedsOrderedByDataEHora();
	}
	@PostConstruct
	public void retrieveAllFeedsOrderedByDataEHora() {
		feeds = em.createNamedQuery("Feed.findAll").getResultList();
	}
}
