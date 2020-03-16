package pmvv.semsa.rh.contrato.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.security.Seguranca;

@Named
@RequestScoped
public class IdleMonitorView {
	@Inject
	private Seguranca seguranca;
	
	public String getNome() {
    	if (seguranca != null) {
    		return seguranca.getUsuario().getNome() + "!";
    	}
    	
    	return "!";
    }
	
	public void onIdle() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sem atividades.", "Você ficou ausente por mais de 5 minutos!"));
    }
 
    public void onActive() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bem vindo de volta.", "Bem vindo de volta " + getNome()));
    }
}