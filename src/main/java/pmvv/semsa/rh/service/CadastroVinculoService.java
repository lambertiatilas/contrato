package pmvv.semsa.rh.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.model.Status;
import pmvv.semsa.rh.model.Vinculo;
import pmvv.semsa.rh.repository.Vinculos;
import pmvv.semsa.rh.util.jpa.Transactional;

public class CadastroVinculoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Vinculos vinculos;
	
	@Transactional
	public Vinculo salvar(Profissional profissional, Vinculo vinculo) throws NegocioException {
		if (vinculo.isNaoSalvarEfetivoAtivo()) {
			throw new NegocioException("Não é possível salvar um vínculo [Efetivo] de status ativo com uma data fim.");
		}
		
		if (vinculo.isNaoSalvarNaoEfetivoAtivo()) {
			throw new NegocioException("Não é possível salvar um vínculo [" + vinculo.getTipo().getDescricao() + "] de status ativo sem data fim.");
		}
		
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
		
		if (vinculo.isExistente() && vinculo.isInativo()) {
			vinculo = vinculos.porId(vinculo.getId());
			vinculo.setStatus(Status.INATIVO);
			vinculo.setDataFim(new Date());
			vinculo.inativarLotacoes();
		}
		
		return vinculos.guardar(vinculo);
	}
}