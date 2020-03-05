package pmvv.semsa.rh.contrato.controller;

import pmvv.semsa.rh.contrato.model.Lotacao;

public class EventLotacaoAlterada {
	
	private Lotacao lotacao;
	
	public EventLotacaoAlterada(Lotacao lotacao) {
		this.lotacao = lotacao;
	}

	public Lotacao getLotacao() {
		return lotacao;
	}
}