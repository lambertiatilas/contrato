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
import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.model.StatusSolicitacao;
import pmvv.semsa.rh.contrato.repository.Estabelecimentos;
import pmvv.semsa.rh.contrato.repository.Profissionais;
import pmvv.semsa.rh.contrato.repository.Solicitacoes;
import pmvv.semsa.rh.contrato.repository.filter.SolicitacaoFilter;

@Named
@ViewScoped
public class PesquisaAtendimentosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Solicitacoes solicitacoes;	
	private SolicitacaoFilter filtro;
	private LazyDataModel<Solicitacao> model;
	private Solicitacao solicitacaoSelecionada;
	@Inject
	private Estabelecimentos estabelecimentos;
	@Inject
	private Profissionais profissionais;
	private List<Estabelecimento> listaEstabelecimentosSolicitantes = new ArrayList<>();
	private List<Profissional> listaProfissionaisSolicitantes = new ArrayList<>();
	private List<Estabelecimento> listaEstabelecimentosAtendentes = new ArrayList<>();
	private List<Profissional> listaProfissionaisAtendentes = new ArrayList<>();

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
	
	public List<Estabelecimento> getListaEstabelecimentosSolicitantes() {
		return listaEstabelecimentosSolicitantes;
	}

	public List<Profissional> getListaProfissionaisSolicitantes() {
		return listaProfissionaisSolicitantes;
	}

	public List<Estabelecimento> getListaEstabelecimentosAtendentes() {
		return listaEstabelecimentosAtendentes;
	}

	public List<Profissional> getListaProfissionaisAtendentes() {
		return listaProfissionaisAtendentes;
	}

	public StatusSolicitacao[] getStatuses() {
		return StatusSolicitacao.values();
	}

	public void preRender() {
		filtro = new SolicitacaoFilter();
		listaEstabelecimentosSolicitantes = estabelecimentos.estabelecimentosSolicitantes();
		listaProfissionaisSolicitantes = profissionais.profissionaisSolicitantes();
		listaEstabelecimentosAtendentes = estabelecimentos.estabelecimentosAtendentes();
		listaProfissionaisAtendentes = profissionais.profissionaisAtendentes();
		pesquisar();
	}

	private void pesquisar() {
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
}