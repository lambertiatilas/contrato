package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.repository.Solicitacoes;
import pmvv.semsa.rh.contrato.repository.filter.SolicitacaoFilter;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaSolicitacoesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Solicitacoes solicitacoes;	
	private SolicitacaoFilter filtro;
	private LazyDataModel<Solicitacao> model;
	private Solicitacao solicitacaoSelecionada;
	
	public PesquisaSolicitacoesBean() {
		filtro = new SolicitacaoFilter();
	}

	public SolicitacaoFilter getFiltro() {
		return filtro;
	}

	public LazyDataModel<Solicitacao> getModel() {
		return model;
	}

	public Solicitacao getSolicitacaoSelecionada() {
		return solicitacaoSelecionada;
	}

	public void setSolicitacaoSelecionada(Solicitacao solicitacaoSelecionada) {
		this.solicitacaoSelecionada = solicitacaoSelecionada;
	}

	public void pesquisar() {
		model = new LazyDataModel<Solicitacao>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Solicitacao> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(solicitacoes.quantidadeFiltradas(filtro));
				return solicitacoes.filtradas(filtro);
			}
		};
	}
	
	public void excluir() {
		try {
			solicitacoes.remover(solicitacaoSelecionada);
			FacesUtil.addInfoMessage("Solicitacão " + solicitacaoSelecionada.getId() + " excluída com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}