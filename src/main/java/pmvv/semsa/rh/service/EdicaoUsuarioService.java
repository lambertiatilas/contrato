package pmvv.semsa.rh.service;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.model.Status;
import pmvv.semsa.rh.repository.Profissionais;
import pmvv.semsa.rh.util.jpa.Transactional;

public class EdicaoUsuarioService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Profissionais profissionais;
	
	@Transactional
	public Profissional ativarOuInativar(Profissional profissional) throws NegocioException {
		profissional = profissionais.porId(profissional.getId());
		
		if (profissional.isNaoAlteravel()) {
			throw new NegocioException("O status do usuário não pode ser alterado!");
		}
		
		if (profissional.isInativavel()) {
			profissional.setStatus(Status.INATIVO);
		} else if (profissional.isAtivavel()) {
			profissional.setStatus(Status.ATIVO);
		}
		
		return profissionais.guardar(profissional);
	}
	
	@Transactional
	public Profissional redefinirSenha(Profissional profissional) throws NegocioException {
		profissional = profissionais.porId(profissional.getId());
		
		if (profissional.isNaoAlteravel()) {
			throw new NegocioException("A senha do usuário não pode ser resetada!");
		}
		
		profissional.setSenha(profissional.gerarSenha());
		return profissionais.guardar(profissional);
	}
}