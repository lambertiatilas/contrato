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
	public Solicitacao enviar(Solicitacao solicitacao) throws NegocioException {
		solicitacao = cadastroSolicitacaoService.salvar(solicitacao);
		
		if (solicitacao.isNaoEnviavel()) {
			throw new NegocioException("Solicitação não pode ser enviada!");
		}
		
		solicitacao.setProfissionalSolicitante(seguranca.getUsuario());
		solicitacao.setEstabelecimentoSolicitante(seguranca.getUsuario().getLocalAcesso());
		solicitacao.setStatus(StatusSolicitacao.ENVIADA);
		return solicitacoes.guardar(solicitacao);
	}
	
	@Transactional
	public Solicitacao autorizar(Solicitacao solicitacao) throws NegocioException {
		solicitacao = cadastroSolicitacaoService.salvar(solicitacao);
		
		if (solicitacao.isNaoAtendivel()) {
			throw new NegocioException("Solicitação não pode ser atendida!");
		}
		
		solicitacao.setProfissionalAutorizante(seguranca.getUsuario());
		solicitacao.setEstabelecimentoAutorizante(seguranca.getUsuario().getLocalAcesso());
		solicitacao.setStatus(StatusSolicitacao.AUTORIZADA);
		return solicitacoes.guardar(solicitacao);
	}
	
	@Transactional
	public Solicitacao devolver(Solicitacao solicitacao) throws NegocioException {
		solicitacao = solicitacoes.porId(solicitacao.getId());
		
		if (solicitacao.isNaoDevolvivel()) {
			throw new NegocioException("Solicitação não pode ser devolvida!");
		}
		
		solicitacao.getLotacoes().clear();
		solicitacao.setStatus(StatusSolicitacao.NAO_ENVIADA);
		return solicitacoes.guardar(solicitacao);
	}
	
	@Transactional
	public Solicitacao atender(Solicitacao solicitacao) throws NegocioException {
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