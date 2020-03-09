package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.service.EdicaoUsuarioService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@RequestScoped
public class EdicaoUsuarioBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EdicaoUsuarioService edicaoUsuarioService;
	@Inject
	@Edicao
	private Profissional profissional;
	@Inject
	private Event<EventUsuarioAlterado> eventUsuarioAlterado;
	
	public void ativarOuInativar() {
		try {
			profissional = edicaoUsuarioService.ativarOuInativar(profissional);
			eventUsuarioAlterado.fire(new EventUsuarioAlterado(profissional));
			FacesUtil.addInfoMessage("O status do usuário foi alterado para " + profissional.getStatus().getDescricao() + " !");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void redefinirSenha() {
		try {
			profissional = edicaoUsuarioService.redefinirSenha(profissional);
			eventUsuarioAlterado.fire(new EventUsuarioAlterado(profissional));
			FacesUtil.addInfoMessage("Senha resetada com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}