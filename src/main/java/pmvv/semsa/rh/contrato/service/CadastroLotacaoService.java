package pmvv.semsa.rh.contrato.service;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.Status;
import pmvv.semsa.rh.contrato.model.Vinculo;
import pmvv.semsa.rh.contrato.repository.Lotacoes;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class CadastroLotacaoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Lotacoes lotacoes;
	
	@Transactional
	public Lotacao salvar(Vinculo vinculo, Lotacao lotacao) throws NegocioException {
		
		if (lotacao.isNovo()) {
			lotacao.setStatus(Status.ATIVO);
			lotacao.setVinculo(vinculo);
		}
		
		return lotacoes.guardar(lotacao);
	}
}