package pmvv.semsa.rh.contrato.service;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.model.StatusSolicitacao;
import pmvv.semsa.rh.contrato.repository.Solicitacoes;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class EdicaoSolicitacaoService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroSolicitacaoService cadastroSolicitacaoService;
	@Inject
	private Solicitacoes solicitacoes;

	@Transactional
	public Solicitacao enviarSolicitacao(Solicitacao solicitacao) throws NegocioException {
		solicitacao = cadastroSolicitacaoService.salvar(solicitacao);
		
		if (solicitacao.isRequisicaoNaoSalvavel()) {
			throw new NegocioException("Solicitação não pode ser enviada!");
		}
		
		solicitacao.setStatus(StatusSolicitacao.ENVIADA);
		return solicitacoes.guardar(solicitacao);
	}
	
	@Transactional
	public Solicitacao atenderSolicitacao(Solicitacao solicitacao) throws NegocioException {
		solicitacao = cadastroSolicitacaoService.salvar(solicitacao);
		
		if (solicitacao.isAtendimentoNaoSalvavel()) {
			throw new NegocioException("Solicitação não pode ser atendida!");
		}
		
		solicitacao.setStatus(StatusSolicitacao.ATENDIDA);
		return solicitacoes.guardar(solicitacao);
	}
}