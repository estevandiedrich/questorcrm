package br.com.questor.crm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Transient;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.questor.crm.enums.LeadEnum;

@Entity
@Table(name = "lead",indexes = {
		@Index(columnList = "id", name = "lead_id_idx"),
		@Index(columnList = "nome", name = "lead_nome_idx")
		}
)
@NamedQueries(value = {
		@NamedQuery(name = "Lead.findByIds",query = "SELECT l FROM Lead l JOIN FETCH l.uf JOIN FETCH l.cidade JOIN FETCH l.usuarioQueCadastrou LEFT JOIN FETCH l.origem LEFT JOIN FETCH l.atividade WHERE l.id IN (:leads) ORDER BY l.nome"),
		@NamedQuery(name = "Conta.findByIds",query = "SELECT l FROM Lead l JOIN FETCH l.uf JOIN FETCH l.cidade JOIN FETCH l.usuarioQueCadastrou LEFT JOIN FETCH l.origem LEFT JOIN FETCH l.atividade WHERE l.statusLead = br.com.questor.crm.enums.LeadEnum.CONTA AND l.id IN (:leads) ORDER BY l.nome"),
		@NamedQuery(name = "Lead.findLeadsEmAvaliacao",query = "SELECT l FROM Lead l JOIN FETCH l.uf JOIN FETCH l.cidade WHERE l.statusLead = br.com.questor.crm.enums.LeadEnum.EM_AVALIACAO ORDER BY l.dataCadastro DESC"),
		@NamedQuery(name = "Lead.findByIdsSemUFCidade",query = "SELECT l FROM Lead l WHERE l.id IN (:leads) ORDER BY l.nome"),
		@NamedQuery(name = "Conta.findByIdsSemUFCidade",query = "SELECT l FROM Lead l WHERE l.statusLead = br.com.questor.crm.enums.LeadEnum.CONTA AND l.id IN (:leads) ORDER BY l.nome"),
		@NamedQuery(name = "Distribuidor.findByIdsSemUFCidade",query = "SELECT l FROM Lead l WHERE l.statusLead = br.com.questor.crm.enums.LeadEnum.DISTRIBUIDOR AND l.id IN (:leads) ORDER BY l.nome"),
		@NamedQuery(name = "Lead.findAll",query = "SELECT l FROM Lead l JOIN FETCH l.uf JOIN FETCH l.cidade JOIN FETCH l.usuarioQueCadastrou LEFT JOIN FETCH l.origem LEFT JOIN FETCH l.atividade ORDER BY l.nome"),
		@NamedQuery(name = "Conta.findAll",query = "SELECT l FROM Lead l JOIN FETCH l.uf JOIN FETCH l.cidade JOIN FETCH l.usuarioQueCadastrou LEFT JOIN FETCH l.origem LEFT JOIN FETCH l.atividade WHERE l.statusLead = br.com.questor.crm.enums.LeadEnum.CONTA ORDER BY l.nome"),
		@NamedQuery(name = "Lead.findAllSemUFCidade",query = "SELECT l FROM Lead l ORDER BY l.nome"),
		@NamedQuery(name = "Conta.findAllSemUFCidade",query = "SELECT l FROM Lead l WHERE l.statusLead = br.com.questor.crm.enums.LeadEnum.CONTA ORDER BY l.nome"),
		@NamedQuery(name = "Distribuidor.findAllSemUFCidade",query = "SELECT l FROM Lead l WHERE l.statusLead = br.com.questor.crm.enums.LeadEnum.DISTRIBUIDOR ORDER BY l.nome"),
		@NamedQuery(name = "Lead.findImagemById",query = "SELECT l.imagem FROM Lead l WHERE l.id = :id")
		}
)
@XmlRootElement
@SequenceGenerator(name="LEAD_SEQUENCE", sequenceName="LEAD_SEQUENCE", allocationSize=1, initialValue=1)
public class Lead implements Serializable {
	public Lead()
	{
		this.leadPai = null;
		
		this.statusLead = LeadEnum.EM_AVALIACAO;
		this.dataCadastro = new Date();
		this.gruposUsuarios = new ArrayList<GrupoUsuariosLead>();
		this.grupoUsuariosSelecionado = new GrupoUsuarios();
		this.newAtividadeAgenda = new AtividadeAgenda();
		this.atividadesAgenda = new ArrayList<AtividadeAgenda>();
		this.emails = new ArrayList<Email>();
		this.contatos = new ArrayList<Contato>();
		this.anexos = new ArrayList<Anexo>();
		this.contatoSelecionado = new Contato();
		this.uf = new UF();
		this.cidade = new Cidade();
		this.cidadesPorUf = new ArrayList<Cidade>();
		this.notas = new ArrayList<Nota>();
		this.origem = new Origem();
		this.atividade = new Atividade();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -2921454420621654968L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="LEAD_SEQUENCE")
	private Long id;
	
	@NotNull
	@NotEmpty
	private String nome;
	
	private LeadEnum statusLead;
	
	@OneToMany(mappedBy = "lead", targetEntity = Contato.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Contato> contatos;
	
	@OneToMany(mappedBy = "lead", targetEntity = Nota.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Nota> notas;
	
	private String site;
	
	@Column(length=2000)
	private String observacao;
	
	private String numero;
	
	private String logradouro;
	
	private String endereco;
	
	private String bairro;
	
	private String quantidadeFuncionarios;
	
	private String cep;
	@ManyToOne(cascade=CascadeType.DETACH,fetch = FetchType.LAZY)
	  @JoinColumn(name = "cidade_id")
	private Cidade cidade;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "uf_id")
	private UF uf;
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "origem_id")
	private Origem origem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "atividade_id")
	private Atividade atividade;
	
	@Transient
	private Contato contatoSelecionado;
	
	@Transient
	private GrupoUsuarios grupoUsuariosSelecionado;
	
	@Transient
	private AtividadeAgenda newAtividadeAgenda;

	@OneToMany(mappedBy = "lead", targetEntity = AtividadeAgenda.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<AtividadeAgenda> atividadesAgenda;
	
	private Date dataCadastro;
	
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "leadpai_id",referencedColumnName = "id", nullable=true)
	private Lead leadPai;
	
//	@ManyToMany
//    @JoinTable(name="grupousuarios_lead", joinColumns={@JoinColumn(name="lead_id")}, inverseJoinColumns={@JoinColumn(name="grupousuarios_id")})
	@OneToMany(mappedBy = "lead", targetEntity = GrupoUsuariosLead.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<GrupoUsuariosLead> gruposUsuarios;
	
	@OneToMany(mappedBy = "lead", targetEntity = Email.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Email> emails; 
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	  @JoinColumn(name = "imagem_id")
	private Arquivo imagem;
	
	@Transient
	private Part imagemPart;
	
	@Transient
	private List<Cidade> cidadesPorUf;
	
	@OneToMany(mappedBy = "lead", targetEntity = Anexo.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Anexo> anexos;
	
	@ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "principals_id")
	private Principals usuarioQueCadastrou;
	
	private int tipoDocumento;
	
	private String documento;
	@Column(length=13,precision=2)
	private BigDecimal receitaAnual;
	
	public LeadEnum[] getStatusLeadValues()
	{
		return LeadEnum.values();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Email> getEmails() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	public Arquivo getImagem() {
		return imagem;
	}

	public void setImagem(Arquivo imagem) {
		this.imagem = imagem;
	}

	public Part getImagemPart() {
		return imagemPart;
	}

	public void setImagemPart(Part imagemPart) {
		this.imagemPart = imagemPart;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	public List<GrupoUsuariosLead> getGruposUsuarios() {
		return gruposUsuarios;
	}

	public void setGruposUsuarios(List<GrupoUsuariosLead> grupoUsuarios) {
		this.gruposUsuarios = grupoUsuarios;
	}

	public List<Anexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<Anexo> anexos) {
		this.anexos = anexos;
	}

	public Lead getLeadPai() {
		return leadPai;
	}

	public void setLeadPai(Lead leadPai) {
		this.leadPai = leadPai;
	}

	public Contato getContatoSelecionado() {
		return contatoSelecionado;
	}

	public void setContatoSelecionado(Contato contatoSelecionado) {
		this.contatoSelecionado = contatoSelecionado;
	}

	public GrupoUsuarios getGrupoUsuariosSelecionado() {
		return grupoUsuariosSelecionado;
	}

	public void setGrupoUsuariosSelecionado(GrupoUsuarios grupoUsuariosSelecionado) {
		this.grupoUsuariosSelecionado = grupoUsuariosSelecionado;
	}

	public Principals getUsuarioQueCadastrou() {
		return usuarioQueCadastrou;
	}

	public void setUsuarioQueCadastrou(Principals usuarioQueCadastrou) {
		this.usuarioQueCadastrou = usuarioQueCadastrou;
	}

	public AtividadeAgenda getNewAtividadeAgenda() {
		return newAtividadeAgenda;
	}

	public void setNewAtividadeAgenda(AtividadeAgenda newAtividadeAgenda) {
		this.newAtividadeAgenda = newAtividadeAgenda;
	}

	public List<AtividadeAgenda> getAtividadesAgenda() {
		return atividadesAgenda;
	}

	public void setAtividadesAgenda(List<AtividadeAgenda> atividadesAgenda) {
		this.atividadesAgenda = atividadesAgenda;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public LeadEnum getStatusLead() {
		return statusLead;
	}

	public void setStatusLead(LeadEnum statusLead) {
		this.statusLead = statusLead;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public UF getUf() {
		return uf;
	}

	public void setUf(UF uf) {
		this.uf = uf;
	}

	public List<Cidade> getCidadesPorUf() {
		return cidadesPorUf;
	}

	public void setCidadesPorUf(List<Cidade> cidadesPorUf) {
		this.cidadesPorUf = cidadesPorUf;
	}

	public List<Nota> getNotas() {
		return notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	public int getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(int tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public String getQuantidadeFuncionarios() {
		return quantidadeFuncionarios;
	}

	public void setQuantidadeFuncionarios(String quantidadeFuncionarios) {
		this.quantidadeFuncionarios = quantidadeFuncionarios;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public BigDecimal getReceitaAnual() {
		return receitaAnual;
	}

	public void setReceitaAnual(BigDecimal receitaAnual) {
		this.receitaAnual = receitaAnual;
	}
	
}
