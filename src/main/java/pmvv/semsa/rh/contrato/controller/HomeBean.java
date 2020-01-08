package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.repository.Solicitacoes;

@Named
@ViewScoped
public class HomeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Solicitacoes solicitacoes;
	private List<Solicitacao> solicitacoesEnviadas;
	private List<Solicitacao> solicitacoesAtendidas;
	
	public List<Solicitacao> getSolicitacoesEnviadas() {
		return solicitacoesEnviadas;
	}

	public List<Solicitacao> getSolicitacoesAtendidas() {
		return solicitacoesAtendidas;
	}

	public void preRender() {
		solicitacoesEnviadas = solicitacoes.solicitacoesEnviadas();
		solicitacoesAtendidas = solicitacoes.solicitacoesAtendidas();
	}
}