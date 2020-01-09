package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.repository.Estabelecimentos;
import pmvv.semsa.rh.contrato.repository.filter.EstabelecimentoFilter;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEstabelecimentosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Estabelecimentos estabelecimentos;
	private EstabelecimentoFilter filtro;
	private LazyDataModel<Estabelecimento> model;
	private Estabelecimento estabelecimentoSelecionado;
	
	public PesquisaEstabelecimentosBean() {
		filtro = new EstabelecimentoFilter();
	}

	public EstabelecimentoFilter getFiltro() {
		return filtro;
	}
	
	public LazyDataModel<Estabelecimento> getModel() {
		return model;
	}

	public Estabelecimento getEstabelecimentoSelecionado() {
		return estabelecimentoSelecionado;
	}

	public void setEstabelecimentoSelecionado(Estabelecimento estabelecimentoSelecionado) {
		this.estabelecimentoSelecionado = estabelecimentoSelecionado;
	}

	public void pesquisar() {
		model = new LazyDataModel<Estabelecimento>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Estabelecimento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(estabelecimentos.quantidadeFiltrados(filtro));
				return estabelecimentos.filtrados(filtro);
			}
		};
	}
	
	public void excluir() {
		try {
			estabelecimentos.remover(estabelecimentoSelecionado);
			FacesUtil.addInfoMessage("Estabelecimento " + estabelecimentoSelecionado.getDescricao() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}