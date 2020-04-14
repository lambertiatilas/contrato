package pmvv.semsa.rh.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import pmvv.semsa.rh.model.Profissional;

@Named
@RequestScoped
public class Seguranca {

	@Inject
	private ExternalContext externalContext;
	
	private UsuarioSistema usuarioLogado = getUsuarioLogado();
	
	public Profissional getUsuario() {
		Profissional usuario = null;

		if (usuarioLogado != null) {
			usuario = usuarioLogado.getUsuario();
		}
		
		return usuario;
	}
	
	@Produces
	@UsuarioLogado
	public UsuarioSistema getUsuarioLogado() {
		UsuarioSistema usuario = null;
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

		if (auth != null && auth.getPrincipal() != null) {
			usuario = (UsuarioSistema) auth.getPrincipal();
		}
		
		return usuario;
	}
	
	public boolean isAdministradores() {
		return externalContext.isUserInRole("ADMINISTRADORES");
	}
	
	public boolean isAtendentes() {
		return externalContext.isUserInRole("ATENDENTES");
	}
	
	public boolean isSolicitantes() {
		return externalContext.isUserInRole("SOLICITANTES");
	}
	
	public boolean isFuncionarios() {
		return externalContext.isUserInRole("FUNCIONARIOS");
	}
	
	public boolean isAutorizantes() {
		return externalContext.isUserInRole("AUTORIZANTES");
	}
}