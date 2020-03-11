package pmvv.semsa.rh.contrato.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.model.Status;
import pmvv.semsa.rh.contrato.model.Vinculo;
import pmvv.semsa.rh.contrato.repository.Vinculos;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class CadastroVinculoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Vinculos vinculos;
	
	@Transactional
	public Vinculo salvar(Profissional profissional, Vinculo vinculo) throws NegocioException {
		Vinculo vinculoExiste = vinculos.porMatricula(vinculo.getMatricula());
		
		if (vinculoExiste != null && !vinculoExiste.equals(vinculo)) {
			throw new NegocioException("Já existe um vínculo cadastrado com a matrícula informada.");
		}
		
		Long quantidadeVinculosAtivos = vinculos.quantidadeVinculosAtivos(profissional);
		
		if (vinculo.isAtivo() && quantidadeVinculosAtivos >= 2) {
			throw new NegocioException("O profissional já possui " + quantidadeVinculosAtivos + " ativos!");
		}
		
		if (vinculo.isNovo()) {
			vinculo.setStatus(Status.ATIVO);
			vinculo.setProfissional(profissional);
		}
		
		if (vinculo.isExistente() && vinculo.isAtivo()) {
			vinculo.setDataFim(null);
		}
		
		if (vinculo.isExistente() && vinculo.isInativo()) {
			vinculo = vinculos.porId(vinculo.getId());
			vinculo.setStatus(Status.INATIVO);
			vinculo.setDataFim(new Date());
			vinculo.inativarLotacoes();
		}
		
		return vinculos.guardar(vinculo);
	}
}