package pmvv.semsa.rh.contrato.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.model.StatusSolicitacao;
import pmvv.semsa.rh.contrato.repository.Solicitacoes;
import pmvv.semsa.rh.contrato.security.Seguranca;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class CadastroSolicitacaoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Seguranca seguranca;
	
	@Inject
	private Solicitacoes solicitacoes;
	
	@Transactional
	public Solicitacao salvar(Solicitacao solicitacao) throws NegocioException {
		Solicitacao solicitacaoExiste = solicitacoes.existe(seguranca.getUsuario().getLocalAcesso());
		
		if (solicitacaoExiste != null && !solicitacaoExiste.equals(solicitacao)) {
			throw new NegocioException("Existe uma solicitação [não encerrada] para o estabelecimento " + solicitacao.getEstabelecimentoSolcitante().getDescricao() + ".");
		}
		
		if (solicitacao.isNovo()) {
			solicitacao.setDataHoraAbertura(new Date());
			solicitacao.setProfissionalSolicitante(seguranca.getUsuario());
			solicitacao.setEstabelecimentoSolcitante(seguranca.getUsuario().getLocalAcesso());
			solicitacao.setStatus(StatusSolicitacao.NAO_ENVIADA);
		}
		
		return solicitacoes.guardar(solicitacao);
	}
}