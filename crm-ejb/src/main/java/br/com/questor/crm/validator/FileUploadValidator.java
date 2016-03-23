package br.com.questor.crm.validator;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.servlet.http.Part;

//@FacesValidator(value="fileUploadValidator")
@Named("fileUploadValidator")
public class FileUploadValidator implements Validator {

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		Part file = (Part) value;
		if(file != null)
		{
			if (file.getSize() > 1024) {
				msgs.add(new FacesMessage("Imagem muito grande"));
			}
	//		if (!"image/jpeg".equals(file.getContentType())) {
	//			msgs.add(new FacesMessage("Este arquivo não é uma imagem"));
	//		}
			if (!msgs.isEmpty()) {
				throw new ValidatorException(msgs);
			}
		}
	}

}
