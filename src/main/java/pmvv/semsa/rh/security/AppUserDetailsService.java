package pmvv.semsa.rh.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.repository.Profissionais;
import pmvv.semsa.rh.util.cdi.CDIServiceLocator;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
		Profissionais profissionais = CDIServiceLocator.getBean(Profissionais.class);
		Profissional profissional = profissionais.ativo(cpf);
		
		UsuarioSistema autenticado = null;
		
		if (profissional != null) {
			autenticado = new UsuarioSistema(profissional, getGrupos(profissional));
		} else {
			throw new UsernameNotFoundException("Usuário não encontrado.");
		}
		
		return autenticado;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Profissional profissional) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + profissional.getGrupo().getNome().toUpperCase()));
		return authorities;
	}
}