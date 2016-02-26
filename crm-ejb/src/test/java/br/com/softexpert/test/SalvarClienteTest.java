package br.com.softexpert.test;

import static org.junit.Assert.assertNotNull;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.questor.crm.controller.SalvarCliente;
import br.com.questor.crm.model.Cliente;
import br.com.questor.crm.util.Resources;

@RunWith(Arquillian.class)
public class SalvarClienteTest {
	@Deployment
	   public static Archive<?> createTestArchive() {
	      return ShrinkWrap.create(WebArchive.class, "test.war")
	            .addClasses(Cliente.class, SalvarCliente.class, Resources.class)
	            .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
	            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	   }

	   @Inject
	   SalvarCliente salvarCliente;

	   @Inject
	   Logger log;

	   @Test
	   public void testRegister() throws Exception {
	      Cliente newCliente = salvarCliente.getNewCliente();
	      newCliente.setNome("Estevan");
	      salvarCliente.salvar();
	      assertNotNull(newCliente.getId());
	      log.info(newCliente.getNome() + " foi salvo com o id " + newCliente.getId());
	   }
}
