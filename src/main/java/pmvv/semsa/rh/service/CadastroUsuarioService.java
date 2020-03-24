package pmvv.semsa.rh.service;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.model.Status;
import pmvv.semsa.rh.repository.Profissionais;
import pmvv.semsa.rh.util.jpa.Transactional;

public class CadastroUsuarioService implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	@Inject
	private Profissionais profissionais;
	
	@Transactional
	public Profissional salvar(Profissional profissional) throws NegocioException {
		Profissional profissionalExiste = profissionais.porCpf(profissional.getCpf());
		
		if (profissionalExiste != null && !profissionalExiste.equals(profissional)) {
			throw new NegocioException("Já existe uma usuário cadastrado com o cpf informado.");
		}
		
		if (profissional.isNovo()) {
			profissional.setSenha(profissional.gerarSenha());
			profissional.setStatus(Status.ATIVO);
		}
		
		return profissionais.guardar(profissional);
	}
}