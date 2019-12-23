package pmvv.semsa.rh.contrato.service;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.repository.Profissionais;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class CadastroProfissionalService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Profissionais profissionais;
	
	@Transactional
	public Profissional salvar(Profissional profissional) throws NegocioException {
		Profissional profissionalExiste = profissionais.porNome(profissional.getNome());
		
		if (profissionalExiste != null && !profissionalExiste.equals(profissional)) {
			throw new NegocioException("O profissional " + profissional.getNome() + " já esta cadastrado.");
		}
		
		return profissionais.guardar(profissional);
	}
}