package pmvv.semsa.rh.controller;

import pmvv.semsa.rh.model.Solicitacao;

public class EventSolicitacaoAlterada {
	
	private Solicitacao solicitacao;
	
	public EventSolicitacaoAlterada(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public Solicitacao getSolicitacao() {
		return solicitacao;
	}
}