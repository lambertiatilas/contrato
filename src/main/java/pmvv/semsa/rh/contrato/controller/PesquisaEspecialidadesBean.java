package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Especialidade;
import pmvv.semsa.rh.contrato.repository.Especialidades;
import pmvv.semsa.rh.contrato.repository.filter.EspecialidadeFilter;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEspecialidadesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Especialidades especialidades;
	
	private EspecialidadeFilter filtro;
	private List<Especialidade> especialidadesFiltradas;
	
	private Especialidade especialidadeSelecionada;
	
	public PesquisaEspecialidadesBean() {
		filtro = new EspecialidadeFilter();
	}
	
	public EspecialidadeFilter getFiltro() {
		return filtro;
	}

	public List<Especialidade> getEspecialidadesFiltradas() {
		return especialidadesFiltradas;
	}

	public Especialidade getEspecialidadeSelecionada() {
		return especialidadeSelecionada;
	}

	public void setEspecialidadeSelecionada(Especialidade especialidadeSelecionada) {
		this.especialidadeSelecionada = especialidadeSelecionada;
	}
	
	public void pesquisar() {
		especialidadesFiltradas = especialidades.filtradas(filtro);
	}
	
	public void excluir() {
		try {
			especialidades.remover(especialidadeSelecionada);
			especialidadesFiltradas.remove(especialidadeSelecionada);
			FacesUtil.addInfoMessage("Especialidade " + especialidadeSelecionada.getDescricao() + " excluída com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}