package br.com.questor.crm.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.questor.crm.model.Arquivo;

public class BaseController {

	public StreamedContent carregaImagem(Arquivo imagem) throws IOException
	{
//		FacesContext context = FacesContext.getCurrentInstance();

//	    if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
	        // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
//	        return new DefaultStreamedContent();
//	    }
//	    else {
//			Imagem imagem = newLead.getImagem();
			if(imagem != null && imagem.getContent() != null)
			{
		        StreamedContent streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(imagem.getContent()),imagem.getContentType());
		        return streamedContent;
			}
			else
			{
				return new DefaultStreamedContent();
			}
//	    	BufferedImage bufferedImg = new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g2 = bufferedImg.createGraphics();
//            g2.drawString("This is a text", 0, 10);
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//			ImageIO.write(bufferedImg, "png", os);
//            return new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "image/png");
//	    }
	}
}
