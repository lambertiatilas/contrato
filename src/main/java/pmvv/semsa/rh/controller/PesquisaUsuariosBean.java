package pmvv.semsa.rh.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.model.Status;
import pmvv.semsa.rh.repository.Profissionais;
import pmvv.semsa.rh.repository.filter.ProfissionalFilter;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaUsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Profissionais profissionais;
	private ProfissionalFilter filtro;
	private LazyDataModel<Profissional> model;
	private Profissional profissionalSelecionado;

	public ProfissionalFilter getFiltro() {
		return filtro;
	}

	public LazyDataModel<Profissional> getModel() {
		return model;
	}

	public Profissional getProfissionalSelecionado() {
		return profissionalSelecionado;
	}

	public void setProfissionalSelecionado(Profissional profissionalSelecionado) {
		this.profissionalSelecionado = profissionalSelecionado;
	}
	
	public Status[] getStatuses() {
		return Status.values();
	}
	
	public void preRender() {
		filtro = new ProfissionalFilter();
		filtro.setStatus(Status.ATIVO);
		pesquisar();
	}

	private void pesquisar() {
		model = new LazyDataModel<Profissional>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Profissional> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				setRowCount(profissionais.quantidadeFiltrados(filtro));
				return profissionais.filtrados(filtro);
			}
		};
	}
	
	public void excluir() {
		try {
			profissionais.remover(profissionalSelecionado);
			FacesUtil.addInfoMessage("Usuário " + profissionalSelecionado.getNome() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}