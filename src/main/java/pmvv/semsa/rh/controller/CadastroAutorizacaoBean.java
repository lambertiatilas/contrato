package pmvv.semsa.rh.controller;

import java.io.Serializable;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import pmvv.semsa.rh.model.Solicitacao;

@Named
@ViewScoped
public class CadastroAutorizacaoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Produces
	@EdicaoSolicitacao
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
	
	public void solicitacaoAlterada(@Observes EventSolicitacaoAlterada event) {
		solicitacao = event.getSolicitacao();
	}
}