package pmvv.semsa.rh.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import pmvv.semsa.rh.model.Especialidade;
import pmvv.semsa.rh.repository.Especialidades;
import pmvv.semsa.rh.repository.filter.EspecialidadeFilter;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEspecialidadesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Especialidades especialidades;
	private EspecialidadeFilter filtro;
	private LazyDataModel<Especialidade> model;
	private Especialidade especialidadeSelecionada;
	
	public PesquisaEspecialidadesBean() {
		filtro = new EspecialidadeFilter();
		pesquisar();
	}
	
	public EspecialidadeFilter getFiltro() {
		return filtro;
	}

	public LazyDataModel<Especialidade> getModel() {
		return model;
	}

	public Especialidade getEspecialidadeSelecionada() {
		return especialidadeSelecionada;
	}

	public void setEspecialidadeSelecionada(Especialidade especialidadeSelecionada) {
		this.especialidadeSelecionada = especialidadeSelecionada;
	}
	
	private void pesquisar() {
		model = new LazyDataModel<Especialidade>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Especialidade> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));	
				setRowCount(especialidades.quantidadeFiltradas(filtro));
				return especialidades.filtradas(filtro);
			}
		};
	}
	
	public void excluir() {
		try {
			especialidades.remover(especialidadeSelecionada);
			FacesUtil.addInfoMessage("Especialidade " + especialidadeSelecionada.getDescricao() + " excluída com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}