package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.model.StatusSolicitacao;
import pmvv.semsa.rh.contrato.repository.Estabelecimentos;
import pmvv.semsa.rh.contrato.repository.Solicitacoes;
import pmvv.semsa.rh.contrato.repository.filter.SolicitacaoFilter;
import pmvv.semsa.rh.contrato.security.Seguranca;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaSolicitacoesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Seguranca seguranca;
	
	@Inject
	private Solicitacoes solicitacoes;	
	private SolicitacaoFilter filtro;
	private LazyDataModel<Solicitacao> model;
	private Solicitacao solicitacaoSelecionada;
	
	@Inject
	private Estabelecimentos estabelecimentos;
	private List<Estabelecimento> listaEstabelecimentos = new ArrayList<>();

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
	
	public List<Estabelecimento> getListaEstabelecimentos() {
		return listaEstabelecimentos;
	}
	
	public StatusSolicitacao[] getStatuses() {
		return StatusSolicitacao.values();
	}

	public void preRender() {
		listaEstabelecimentos = estabelecimentos.estabelecimentos();
		filtro = new SolicitacaoFilter();
		
		if (seguranca.getUsuario() != null) {
			filtro.setEstabelecimentoSolicitante(seguranca.getUsuario().getLocalAcesso());
		}
		
		pesquisar();
	}

	private void pesquisar() {
		model = new LazyDataModel<Solicitacao>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Solicitacao> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				
				if (sortField == null) {
					filtro.setAscendente(SortOrder.DESCENDING.equals(sortOrder));
					filtro.setPropriedadeOrdenacao("id");
				} else {
					filtro.setPropriedadeOrdenacao(sortField);
					filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				}
				
				setRowCount(solicitacoes.quantidadeFiltradas(filtro));
				return solicitacoes.filtradas(filtro);
			}
		};
	}
	
	public void excluir() {
		if (solicitacaoSelecionada.isRequisicaoAlteravel()) {
			try {
				solicitacoes.remover(solicitacaoSelecionada);
				FacesUtil.addInfoMessage("Solicitacão " + solicitacaoSelecionada.getId() + " excluída com sucesso.");
			} catch (NegocioException ne) {
				FacesUtil.addErrorMessage(ne.getMessage());
			}
		} else {
			FacesUtil.addErrorMessage("Solicitação não pode ser excluída!");
		}
	}
}