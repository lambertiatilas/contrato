package pmvv.semsa.rh.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import pmvv.semsa.rh.model.Solicitacao;
import pmvv.semsa.rh.model.StatusSolicitacao;
import pmvv.semsa.rh.repository.Solicitacoes;
import pmvv.semsa.rh.security.Seguranca;
import pmvv.semsa.rh.util.jpa.Transactional;

public class CadastroSolicitacaoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Seguranca seguranca;
	@Inject
	private Solicitacoes solicitacoes;
	
	@Transactional
	public Solicitacao salvar(Solicitacao solicitacao) throws NegocioException {
		if (seguranca.getUsuario() == null) {
			throw new NegocioException("Você está logado para salvar a solicitação");
		}
		
		
		if (solicitacao.isRequisicaoSalvavel()) {
			Solicitacao solicitacaoExiste = solicitacoes.existe(seguranca.getUsuario().getLocalAcesso());
			
			if (solicitacaoExiste != null && !solicitacaoExiste.equals(solicitacao)) {
				throw new NegocioException("Existe uma solicitação [não encerrada] para o seu estabelecimento!");
			}
		}
		
		if (solicitacao.isNovo()) {
			solicitacao.setDataHoraAbertura(new Date());
			solicitacao.setProfissionalSolicitante(seguranca.getUsuario());
			solicitacao.setEstabelecimentoSolicitante(seguranca.getUsuario().getLocalAcesso());
			solicitacao.setStatus(StatusSolicitacao.NAO_ENVIADA);
		}
		
		if (solicitacao.isLotacoesNaoPendentes()) {
			solicitacao.setDataHoraEncerramento(new Date());
			solicitacao.setStatus(StatusSolicitacao.FINALIZADA);
		}
		
		return solicitacoes.guardar(solicitacao);
	}
}