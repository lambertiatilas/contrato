package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.service.CadastroSolicitacaoService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

public class CadastroAtendimentoBean implements Serializable {

private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroSolicitacaoService cadastroSolicitacaoService;
	private Solicitacao solicitacao;
	private Lotacao lotacao;
	
	public CadastroAtendimentoBean() {
		limpar();
	}
	
	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public Lotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(Lotacao lotacao) {
		this.lotacao = lotacao;
	}
	
	public void inicializar() {
		if (solicitacao == null) {
			limpar();
		}
	}
	
	private void limpar() {
		solicitacao = new Solicitacao();
		lotacao = new Lotacao();
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