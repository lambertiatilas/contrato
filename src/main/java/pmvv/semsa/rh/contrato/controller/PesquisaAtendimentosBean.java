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
public class PesquisaAtendimentosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Solicitacoes solicitacoes;	
	private List<Solicitacao> solicitacoesEnviadas;

	public List<Solicitacao> getSolicitacoesEnviadas() {
		return solicitacoesEnviadas;
	}

	public void preRender() {
		pesquisar();
	}

	private void pesquisar() {
		solicitacoesEnviadas = solicitacoes.solicitacoesEnviadas();
	}
}