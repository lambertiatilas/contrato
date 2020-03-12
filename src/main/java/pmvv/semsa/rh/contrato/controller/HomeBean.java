package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.repository.Solicitacoes;
import pmvv.semsa.rh.contrato.security.Seguranca;

@Named
@RequestScoped
public class HomeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Seguranca seguranca;
	@Inject
	private Solicitacoes solicitacoes;
	private List<Solicitacao> solicitacoesEnviadas;
	private List<Solicitacao> solicitacoesAtendidas;
	private Solicitacao solicitacaoPendende;
	
	public List<Solicitacao> getSolicitacoesEnviadas() {
		return solicitacoesEnviadas;
	}

	public List<Solicitacao> getSolicitacoesAtendidas() {
		return solicitacoesAtendidas;
	}

	public Solicitacao getSolicitacaoPendende() {
		return solicitacaoPendende;
	}

	public void preRender() {
		if (seguranca.getUsuario() != null) {
			solicitacoesEnviadas = solicitacoes.solicitacoesEnviadas();
			solicitacoesAtendidas = solicitacoes.solicitacoesAtendidas();
			solicitacaoPendende = solicitacoes.solicitacaoPendete(seguranca.getUsuario().getLocalAcesso());
		}
	}
	
	public boolean isExisteSolicitacaoPendete() {
		return seguranca.getUsuario() != null && seguranca.isAtendentes() && (!solicitacoesEnviadas.isEmpty() || !solicitacoesAtendidas.isEmpty());
	}
}