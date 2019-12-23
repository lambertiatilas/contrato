package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Profissional;

@Named
@ViewScoped
public class CadastroProfissionalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Profissional profissional;
	
	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}
	
	public void inicializar() {
		if (profissional == null) {
			limpar();
		}
	}
	
	private void limpar() {
		profissional = new Profissional();
	}

	public void salvar() {
		System.out.println(profissional.getNome());
	}
}