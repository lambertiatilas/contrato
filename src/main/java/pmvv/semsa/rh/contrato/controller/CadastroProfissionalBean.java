package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.service.CadastroProfissionalService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProfissionalBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroProfissionalService cadastroProfissionalService;

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
		try {
			profissional = cadastroProfissionalService.salvar(profissional);
			limpar();	
			FacesUtil.addInfoMessage("Profissional salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}