package br.com.questor.crm.util;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {
	private ExceptionHandlerFactory parent;
	 
	   // this injection handles jsf
	   public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
	    this.parent = parent;
	   }
	 
	    @Override
	    public ExceptionHandler getExceptionHandler() {
	        ExceptionHandler handler = new CustomExceptionHandler(parent.getExceptionHandler());
	        return handler;
	    }
//	se tiver problemas com sessao expirada adicionar o exceptionHandlerFactory no faces-config.xml
//	    <factory>
//        <exception-handler-factory>
//            com.forest.exception.CustomExceptionHandlerFactory
//        </exception-handler-factory>
//		</factory>
}
