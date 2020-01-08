package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
	private List<Solicitacao> solicitacoesFiltradas;
	
	private Solicitacao solicitacaoSelecionada;
	
	public PesquisaSolicitacoesBean() {
		filtro = new SolicitacaoFilter();
	}

	public SolicitacaoFilter getFiltro() {
		return filtro;
	}
	
	public List<Solicitacao> getSolicitacoesFiltradas() {
		return solicitacoesFiltradas;
	}

	public Solicitacao getSolicitacaoSelecionada() {
		return solicitacaoSelecionada;
	}

	public void setSolicitacaoSelecionada(Solicitacao solicitacaoSelecionada) {
		this.solicitacaoSelecionada = solicitacaoSelecionada;
	}

	public void pesquisar() {
		solicitacoesFiltradas = solicitacoes.filtradas(filtro);
	}
	
	public void excluir() {
		try {
			solicitacoes.remover(solicitacaoSelecionada);
			solicitacoesFiltradas.remove(solicitacaoSelecionada);
			FacesUtil.addInfoMessage("Solicitacão " + solicitacaoSelecionada.getId() + " excluída com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}