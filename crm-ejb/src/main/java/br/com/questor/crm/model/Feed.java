package br.com.questor.crm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "feed",indexes = {
		@Index(columnList = "id", name = "feed_id_idx"),
		@Index(columnList = "dataEHora", name = "dataEHora_idx")
		}
)
@XmlRootElement
@NamedQueries(value = {
		@NamedQuery(name = "Feed.findAll",query = "SELECT f FROM Feed AS f JOIN FETCH f.usuarioQueCriou ORDER BY f.dataEHora DESC"),
		@NamedQuery(name = "Feed.findById",query = "SELECT f FROM Feed AS f JOIN FETCH f.usuarioQueCriou WHERE f.id = :id ORDER BY f.dataEHora DESC")
		}
)
@SequenceGenerator(name="FEED_SEQUENCE", sequenceName="FEED_SEQUENCE", allocationSize=1, initialValue=1)
public class Feed implements Serializable{
	public Feed()
	{
		dataEHora = new Date();
		usuarioQueCriou = new Principals();
		pai = null;
		comentarios = new ArrayList<>();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4118718105618136626L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="FEED_SEQUENCE")
	private Long id;
	@NotNull
	@NotEmpty
	private String texto;
	@NotNull
	private Date dataEHora;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "usuario_que_criou_id")
	private Principals usuarioQueCriou;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "feed_pai_id")
	private Feed pai;
	@OneToMany(mappedBy = "pai", targetEntity = Feed.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Feed> comentarios;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Date getDataEHora() {
		return dataEHora;
	}
	public void setDataEHora(Date dataEHora) {
		this.dataEHora = dataEHora;
	}
	public Principals getUsuarioQueCriou() {
		return usuarioQueCriou;
	}
	public void setUsuarioQueCriou(Principals usuarioQueCriou) {
		this.usuarioQueCriou = usuarioQueCriou;
	}
	public Feed getPai() {
		return pai;
	}
	public void setPai(Feed pai) {
		this.pai = pai;
	}
	public List<Feed> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<Feed> comentarios) {
		this.comentarios = comentarios;
	}
}
