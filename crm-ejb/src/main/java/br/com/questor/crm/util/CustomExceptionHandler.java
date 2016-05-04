package br.com.questor.crm.util;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.inject.Inject;

public class CustomExceptionHandler extends ExceptionHandlerWrapper{
	public CustomExceptionHandler(ExceptionHandler exception)
	{
		this.wrapped = exception;
	}
	@Inject
	private Logger log;
	
	private ExceptionHandler wrapped;

	@Override
	public ExceptionHandler getWrapped() {
		// TODO Auto-generated method stub
		return wrapped;
	}
	@Override
	public void handle() throws FacesException {
		 final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
	        while (i.hasNext()) {
	            ExceptionQueuedEvent event = i.next();
	            ExceptionQueuedEventContext context
	                    = (ExceptionQueuedEventContext) event.getSource();

	            // get the exception from context
	            Throwable t = context.getException();

	            final FacesContext fc = FacesContext.getCurrentInstance();
	            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
	            final NavigationHandler nav = fc.getApplication().getNavigationHandler();

	            //here you do what ever you want with exception
	            try {

	                //log error ?
	                //log.log(Level.SEVERE, "Critical Exception!", t);
	            	String errorPageLocation = "/pages/public/login.xhtml";
//	            	String errorPageLocation = fc.getViewRoot().getViewId();
	                if (t instanceof ViewExpiredException) {
	                    requestMap.put("javax.servlet.error.message", "Session expired, try again!");
	                    fc.setViewRoot(fc.getApplication().getViewHandler().createView(fc, errorPageLocation));
	                    fc.getPartialViewContext().setRenderAll(true);
	                    fc.renderResponse();
	                } else {
	                    //redirect error page
	                    requestMap.put("javax.servlet.error.message", t.getMessage());
	                    nav.handleNavigation(fc, null, errorPageLocation);
	                }

	                fc.renderResponse();
	                // remove the comment below if you want to report the error in a jsf error message
	                //JsfUtil.addErrorMessage(t.getMessage());
	            } finally {
	                //remove it from queue
	                i.remove();
	            }
	        }
	        //parent hanle
	        getWrapped().handle();
	}
}
