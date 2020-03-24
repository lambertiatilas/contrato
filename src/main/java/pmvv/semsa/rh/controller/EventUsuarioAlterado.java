package pmvv.semsa.rh.controller;

import pmvv.semsa.rh.model.Profissional;

public class EventUsuarioAlterado {
	
	private Profissional profissional;
	
	public EventUsuarioAlterado(Profissional profissional) {
		this.profissional = profissional;
	}

	public Profissional getProfissional() {
		return profissional;
	}
}