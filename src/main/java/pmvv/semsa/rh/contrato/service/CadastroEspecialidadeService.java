package pmvv.semsa.rh.contrato.service;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Especialidade;
import pmvv.semsa.rh.contrato.repository.Especialidades;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class CadastroEspecialidadeService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Especialidades especialidades;
	
	@Transactional
	public Especialidade salvar(Especialidade especialidade) throws NegocioException {
		Especialidade especialidadeExiste = especialidades.porDescricao(especialidade.getDescricao());
		
		if (especialidadeExiste != null && !especialidadeExiste.equals(especialidade)) {
			throw new NegocioException("A especialidade " + especialidade.getDescricao() + " já esta cadastrada.");
		}
		
		return especialidades.guardar(especialidade);
	}
}