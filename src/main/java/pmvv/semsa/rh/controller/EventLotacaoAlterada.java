package pmvv.semsa.rh.controller;

import pmvv.semsa.rh.model.Lotacao;

public class EventLotacaoAlterada {
	
	private Lotacao lotacao;
	
	public EventLotacaoAlterada(Lotacao lotacao) {
		this.lotacao = lotacao;
	}

	public Lotacao getLotacao() {
		return lotacao;
	}
}