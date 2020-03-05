package pmvv.semsa.rh.contrato.service;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.StatusLotacao;
import pmvv.semsa.rh.contrato.repository.Lotacoes;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class EdicaoLotacaoService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroSolicitacaoService cadastroSolicitacaoService;
	@Inject
	private Lotacoes lotacoes;

	@Transactional
	public Lotacao aceitarLotacao(Lotacao lotacao) throws NegocioException {
		lotacao = lotacoes.porId(lotacao.getId());
		
		if (lotacao.isNaoAceitavelOuNaoRejeitavel()) {
			throw new NegocioException("Lotação não pode ser aceita!");
		}
		
		lotacao.setStatus(StatusLotacao.ATIVO);
		cadastroSolicitacaoService.salvar(lotacao.getSolicitacao());
		return lotacao;
	}
	
	@Transactional
	public Lotacao rejeitarLotacao(Lotacao lotacao) throws NegocioException {
		lotacao = lotacoes.porId(lotacao.getId());
		
		if (lotacao.isNaoAceitavelOuNaoRejeitavel()) {
			throw new NegocioException("Lotação não pode ser rejeitada!");
		}
		
		lotacao.setStatus(StatusLotacao.INATIVO);
		cadastroSolicitacaoService.salvar(lotacao.getSolicitacao());
		return lotacao;
	}
}