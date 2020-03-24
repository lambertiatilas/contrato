package pmvv.semsa.rh.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.model.Vinculo;
import pmvv.semsa.rh.repository.Vinculos;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaVinculosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Vinculos vinculos;
	private Profissional profissional;
	private Vinculo vinculo;

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public Vinculo getVinculo() {
		return vinculo;
	}

	public void setVinculo(Vinculo vinculo) {
		this.vinculo = vinculo;
	}

	public void excluir() {
		try {
			vinculos.remover(vinculo);
			profissional.getVinculos().remove(vinculo);
			FacesUtil.addInfoMessage("Vínculo " + vinculo.getEspecialidade().getDescricao() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}