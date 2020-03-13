package pmvv.semsa.rh.contrato.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Grupo;
import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.model.Status;
import pmvv.semsa.rh.contrato.repository.Grupos;
import pmvv.semsa.rh.contrato.repository.Profissionais;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class CadastroProfissionalService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Profissionais profissionais;
	
	@Inject
	private Grupos grupos;
	private List<Grupo> listaGrupos = new ArrayList<>();
	
	@Transactional
	public Profissional salvar(Profissional profissional) throws NegocioException {
		Profissional profissionalExiste = profissionais.porCpf(profissional.getCpf());
		listaGrupos = grupos.grupos();
		
		if (profissionalExiste != null && !profissionalExiste.equals(profissional)) {
			throw new NegocioException("Já existe um profissional cadastrado com o cpf informado.");
		}
		
		if (profissional.isNovo()) {
			profissional.setSenha(profissional.gerarSenha());
			profissional.setStatus(Status.ATIVO);
			profissional.setGrupo(listaGrupos.get(3));
		}
		
		return profissionais.guardar(profissional);
	}
}