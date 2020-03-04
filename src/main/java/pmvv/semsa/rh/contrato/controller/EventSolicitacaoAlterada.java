package pmvv.semsa.rh.contrato.controller;

import pmvv.semsa.rh.contrato.model.Solicitacao;

public class EventSolicitacaoAlterada {
	
	private Solicitacao solicitacao;
	
	public EventSolicitacaoAlterada(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public Solicitacao getSolicitacao() {
		return solicitacao;
	}
}