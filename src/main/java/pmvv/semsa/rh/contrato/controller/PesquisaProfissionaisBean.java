package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.repository.Profissionais;
import pmvv.semsa.rh.contrato.repository.filter.ProfissionalFilter;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaProfissionaisBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Profissionais profissionais;
	
	private ProfissionalFilter filtro;
	private List<Profissional> profissionaisFiltrados;
	
	private Profissional profissionalSelecionado;
	
	public PesquisaProfissionaisBean() {
		filtro = new ProfissionalFilter();
	}

	public ProfissionalFilter getFiltro() {
		return filtro;
	}
	
	public List<Profissional> getProfissionaisFiltrados() {
		return profissionaisFiltrados;
	}

	public Profissional getProfissionalSelecionado() {
		return profissionalSelecionado;
	}

	public void setProfissionalSelecionado(Profissional profissionalSelecionado) {
		this.profissionalSelecionado = profissionalSelecionado;
	}

	public void pesquisar() {
		profissionaisFiltrados = profissionais.filtrados(filtro);
	}
	
	public void excluir() {
		try {
			profissionais.remover(profissionalSelecionado);
			profissionaisFiltrados.remove(profissionalSelecionado);
			FacesUtil.addInfoMessage("Profissional " + profissionalSelecionado.getNome() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}