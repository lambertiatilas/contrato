package pmvv.semsa.rh.contrato.service;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.model.Status;
import pmvv.semsa.rh.contrato.repository.Profissionais;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class CadastroProfissionalService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Profissionais profissionais;
	
	@Transactional
	public Profissional salvar(Profissional profissional) throws NegocioException {
		Profissional profissionalExiste = profissionais.porCpf(profissional.getCpf());
		
		if (profissionalExiste != null && !profissionalExiste.equals(profissional)) {
			throw new NegocioException("Já existe uma pessoa cadastrada com o cpf informado.");
		}
		
		if (profissional.isNovo()) {
			profissional.setSenha(profissional.gerarSenha());
			profissional.setStatus(Status.ATIVO);
		}
		
		return profissionais.guardar(profissional);
	}
}