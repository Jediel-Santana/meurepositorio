package edu.desafios.sefaz.util;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import edu.desafios.sefaz.modelo.Usuario;

public class Autorizador implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent evento) {
		
		FacesContext context = evento.getFacesContext();
		NavigationHandler handler = context.getApplication().getNavigationHandler();
		
		String nomeDaPagina = context.getViewRoot().getViewId();
		Usuario usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
		
		if((nomeDaPagina.contains("login.xhtml") || nomeDaPagina.contains("novoUsuario.xhtml")) && usuario == null ) {
			return;
		}
		
		if((nomeDaPagina.contains("login.xhtml")) && usuario != null ) {
			
			handler.handleNavigation(context, null, "home?faces-redirect=true");
			context.renderResponse();

			return;
		}
		
		if(usuario != null) {
			return;
		} 
		
		handler.handleNavigation(context, null, "login?faces-redirect=true");
		
		context.renderResponse();
		
	}

	@Override
	public void beforePhase(PhaseEvent event) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
