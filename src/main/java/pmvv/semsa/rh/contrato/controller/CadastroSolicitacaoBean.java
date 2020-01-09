package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.service.CadastroSolicitacaoService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroSolicitacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroSolicitacaoService cadastroSolicitacaoService;
	private Solicitacao solicitacao;
	
	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public void inicializar() {
		if (solicitacao == null) {
			limpar();
		}
	}
	
	private void limpar() {
		solicitacao = new Solicitacao();
	}

	public void salvar() {
		try {
			solicitacao = cadastroSolicitacaoService.salvar(solicitacao);
			limpar();	
			FacesUtil.addInfoMessage("Solicitação salva com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}