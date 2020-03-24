package pmvv.semsa.rh.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.model.Lotacao;
import pmvv.semsa.rh.model.Solicitacao;
import pmvv.semsa.rh.repository.Lotacoes;
import pmvv.semsa.rh.repository.Solicitacoes;
import pmvv.semsa.rh.security.Seguranca;

@Named
@RequestScoped
public class HomeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Seguranca seguranca;
	@Inject
	private Solicitacoes solicitacoes;
	@Inject
	private Lotacoes lotacoes;
	private List<Solicitacao> solicitacoesEnviadas;
	private List<Solicitacao> solicitacoesAtendidas;
	private Solicitacao solicitacaoPendende;
	private List<Lotacao> vinculosProximoFim;
	private List<Lotacao> vinculosProximoFimPorEstabelecimento;
	
	public List<Solicitacao> getSolicitacoesEnviadas() {
		return solicitacoesEnviadas;
	}

	public List<Solicitacao> getSolicitacoesAtendidas() {
		return solicitacoesAtendidas;
	}

	public Solicitacao getSolicitacaoPendende() {
		return solicitacaoPendende;
	}

	public List<Lotacao> getVinculosProximoFim() {
		return vinculosProximoFim;
	}

	public List<Lotacao> getVinculosProximoFimPorEstabelecimento() {
		return vinculosProximoFimPorEstabelecimento;
	}

	public void preRender() {
		if (seguranca.getUsuario() != null && seguranca.isAtendentes()) {
			solicitacoesEnviadas = solicitacoes.solicitacoesEnviadas();
			solicitacoesAtendidas = solicitacoes.solicitacoesAtendidas();
			solicitacaoPendende = solicitacoes.solicitacaoPendete(seguranca.getUsuario().getLocalAcesso());
			vinculosProximoFim = lotacoes.vinculosProximoFim();
		}
		
		if (seguranca.getUsuario() != null && seguranca.isSomenteSolicitantes()) {
			solicitacaoPendende = solicitacoes.solicitacaoPendete(seguranca.getUsuario().getLocalAcesso());
			vinculosProximoFimPorEstabelecimento = lotacoes.vinculosProximoFimPorEstabelecimento(seguranca.getUsuario().getLocalAcesso());
		}
	}
	
	public boolean isExisteSolicitacaoPendete() {
		return seguranca.getUsuario() != null && seguranca.isAtendentes() && (!solicitacoesEnviadas.isEmpty() || !solicitacoesAtendidas.isEmpty());
	}
}