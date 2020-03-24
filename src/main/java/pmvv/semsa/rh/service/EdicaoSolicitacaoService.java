package pmvv.semsa.rh.service;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.model.Solicitacao;
import pmvv.semsa.rh.model.StatusSolicitacao;
import pmvv.semsa.rh.repository.Solicitacoes;
import pmvv.semsa.rh.security.Seguranca;
import pmvv.semsa.rh.util.jpa.Transactional;

public class EdicaoSolicitacaoService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroSolicitacaoService cadastroSolicitacaoService;
	@Inject
	private Solicitacoes solicitacoes;
	@Inject
	private Seguranca seguranca;

	@Transactional
	public Solicitacao enviarSolicitacao(Solicitacao solicitacao) throws NegocioException {
		solicitacao = cadastroSolicitacaoService.salvar(solicitacao);
		
		if (solicitacao.isRequisicaoNaoSalvavel()) {
			throw new NegocioException("Solicitação não pode ser enviada!");
		}
		
		solicitacao.setProfissionalSolicitante(seguranca.getUsuario());
		solicitacao.setEstabelecimentoSolicitante(seguranca.getUsuario().getLocalAcesso());
		solicitacao.setStatus(StatusSolicitacao.ENVIADA);
		return solicitacoes.guardar(solicitacao);
	}
	
	@Transactional
	public Solicitacao cancelarSolicitacao(Solicitacao solicitacao) throws NegocioException {
		solicitacao = solicitacoes.porId(solicitacao.getId());
		
		if (solicitacao.isAtendimentoNaoAlteravel()) {
			throw new NegocioException("Solicitação não pode ser cancelada!");
		}
		
		solicitacao.getLotacoes().clear();
		solicitacao.setStatus(StatusSolicitacao.CANCELADA);
		return solicitacoes.guardar(solicitacao);
	}
	
	@Transactional
	public Solicitacao atenderSolicitacao(Solicitacao solicitacao) throws NegocioException {
		solicitacao = cadastroSolicitacaoService.salvar(solicitacao);
		
		if (solicitacao.isNaoAtendivel()) {
			throw new NegocioException("Solicitação não pode ser atendida!");
		}
		
		solicitacao.setProfissionalAtendente(seguranca.getUsuario());
		solicitacao.setEstabelecimentoAtendente(seguranca.getUsuario().getLocalAcesso());
		solicitacao.setStatus(StatusSolicitacao.ATENDIDA);
		return solicitacoes.guardar(solicitacao);
	}
}