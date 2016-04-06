package br.com.questor.crm.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.primefaces.model.DefaultStreamedContent;

import br.com.questor.crm.data.ContatoListProducer;
import br.com.questor.crm.data.CotacaoListProducer;
import br.com.questor.crm.data.EmailListProducer;
import br.com.questor.crm.data.OportunidadeContatoListProducer;
import br.com.questor.crm.model.Anexo;
import br.com.questor.crm.model.Contato;
import br.com.questor.crm.model.ContatoEmail;
import br.com.questor.crm.model.Cotacao;
import br.com.questor.crm.model.Email;
import br.com.questor.crm.model.Imagem;
import br.com.questor.crm.model.Lead;
import br.com.questor.crm.model.ModuloSelecionado;
import br.com.questor.crm.model.Oportunidade;
import br.com.questor.crm.model.OportunidadeContato;
import br.com.questor.crm.model.ProdutoModulosSelecionados;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

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
	private EmailListProducer emailListProducer;
	
	@Inject
	private LoginBean loginBean;
	
	@Inject
	private SalvarEmail salvarEmail;
	
	@Inject
	private OportunidadeContatoListProducer oportunidadeContatoListProducer;
	
	@Resource(lookup = "java:jboss/datasources/PostgreDS")
	private DataSource dataSource;
	
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
	public String listagem()
	{
		return "/pages/protected/user/listagemoportunidades?faces-redirect=true";
	}
	public String editar(Oportunidade oportunidade)
	{
		setOportunidade(oportunidade);
		return "/pages/protected/user/oportunidade?faces-redirect=true";
	}
	public void enviarProposta(Oportunidade oportunidade)
	{
		setOportunidade(oportunidade);
		ByteArrayOutputStream relatorio = this.gerarRelatorioInterno(oportunidade);
		List<ContatoEmail> contatoEmailList = new ArrayList<>();
		for(OportunidadeContato oportunidadeContatos:oportunidade.getContatos())
		{
			ContatoEmail contatoEmail = new ContatoEmail();
			contatoEmail.setContato(oportunidadeContatos.getContato());
			contatoEmailList.add(contatoEmail);
		}
		
		Imagem anexo = new Imagem();
		anexo.setContentType("application/pdf");
		anexo.setImagem(relatorio.toByteArray());
		anexo.setNome("proposta.pdf");
		anexo.setSize(relatorio.size());
		
		Anexo proposta = new Anexo();
		proposta.setContentType("application/pdf");
		proposta.setDescricao("Proposta Comercial");
		proposta.setSize(relatorio.size());
		proposta.setLead(oportunidade.getConta());
		proposta.setImagem(anexo);
		
		Email emailProposta = new Email();
		oportunidade.getConta().setEmails(emailListProducer.retrieveAllEmailsByLeadOrderedBySentDate(oportunidade.getConta()));
		emailProposta.setLead(oportunidade.getConta());
		emailProposta.setSentDate(new Date());
		emailProposta.setEmailFrom(loginBean.getPrincipalsFromDB().getPrincipalID());
		emailProposta.setEmailTo(contatoEmailList);
		emailProposta.setSubject("Proposta comercial");
		emailProposta.setText("Prezado Segue anexo proposta comercial");
		emailProposta.getAnexos().add(proposta);
		salvarEmail.salvar(emailProposta);
	}
	private ByteArrayOutputStream gerarRelatorioInterno(Oportunidade oportunidade)
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("proposta.jasper");
			Map<String,Object> parameters = new HashMap<>();
			parameters.put("oportunidadeId", oportunidade.getId());
			
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JasperPrint print = JasperFillManager.fillReport(is, parameters, conn);
			JRExporter exporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
		     //JRExporter exporter = new net.sf.jasperreports.engine.export.JRHtmlExporter();
		     //JRExporter exporter = new net.sf.jasperreports.engine.export.JRXlsExporter();
		     //JRExporter exporter = new net.sf.jasperreports.engine.export.JRXmlExporter();
		     //JRExporter exporter = new net.sf.jasperreports.engine.export.JRCsvExporter();
			 exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "proposta.pdf");
		     exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
		     exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		     exporter.exportReport();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream;
	}
	public DefaultStreamedContent gerarProposta(Oportunidade oportunidade)
	{
		InputStream relatorio = new ByteArrayInputStream(this.gerarRelatorioInterno(oportunidade).toByteArray());
		return new DefaultStreamedContent(relatorio, "application/pdf", "proposta.pdf");
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
		if(newOportunidade.getId() == null) {
			em.persist(newOportunidade);
		}
		else {
			em.merge(newOportunidade);
		}
		for(OportunidadeContato oportunidadeContato:newOportunidade.getContatos())
		{
			if(oportunidadeContato.getId() == null)
			{
				oportunidadeContato.setOportunidade(newOportunidade);
				em.persist(oportunidadeContato);
			}
		}
		for(Cotacao cotacao:newOportunidade.getCotacoes())
		{
			if(cotacao.getId() == null) {
				cotacao.setOportunidade(newOportunidade);
				em.persist(cotacao);
			}
			for(ProdutoModulosSelecionados p:cotacao.getProdutosModulosSelecionados())
			{
				if(p.getId() == null) {
					p.setProposta(cotacao);
					em.persist(p);
				}
				for(ModuloSelecionado m:p.getModulosSelecionados())
				{
					if(m.getId() == null) {
						m.setProdutosModulosSelecionados(p);
						em.persist(m);
					}
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
