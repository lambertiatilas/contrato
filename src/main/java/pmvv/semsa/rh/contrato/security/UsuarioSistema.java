package pmvv.semsa.rh.contrato.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import pmvv.semsa.rh.contrato.model.Profissional;

public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;
	
	private Profissional usuario;
	
	public UsuarioSistema(Profissional usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getCpf(), usuario.getSenha(), authorities);
		this.usuario = usuario;
	}

	public Profissional getUsuario() {
		return usuario;
	}

	public void setUsuario(Profissional usuario) {
		this.usuario = usuario;
	}
}