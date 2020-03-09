package pmvv.semsa.rh.contrato.controller;

import pmvv.semsa.rh.contrato.model.Profissional;

public class EventUsuarioAlterado {
	
	private Profissional profissional;
	
	public EventUsuarioAlterado(Profissional profissional) {
		this.profissional = profissional;
	}

	public Profissional getProfissional() {
		return profissional;
	}
}