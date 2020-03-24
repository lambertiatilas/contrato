package pmvv.semsa.rh.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import pmvv.semsa.rh.model.CargaHorariaSemanal;
import pmvv.semsa.rh.model.Especialidade;
import pmvv.semsa.rh.model.Estabelecimento;
import pmvv.semsa.rh.model.Lotacao;
import pmvv.semsa.rh.model.StatusLotacao;
import pmvv.semsa.rh.model.TipoVinculo;
import pmvv.semsa.rh.model.Vinculo;
import pmvv.semsa.rh.repository.Especialidades;
import pmvv.semsa.rh.repository.Estabelecimentos;
import pmvv.semsa.rh.repository.Lotacoes;
import pmvv.semsa.rh.repository.filter.LotacaoFilter;
import pmvv.semsa.rh.security.Seguranca;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaLotacoesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Seguranca seguranca;
	@Inject
	private Especialidades especialidades;
	@Inject
	private Estabelecimentos estabelecimentos;
	@Inject
	private Lotacoes lotacoes;
	private LotacaoFilter filtro;
	private LazyDataModel<Lotacao> model;
	private Vinculo vinculo;
	private Lotacao lotacao;
	private List<Especialidade> listaEspecialidades;
	private List<Estabelecimento> listaEstabelecimentos;
	
	public PesquisaLotacoesBean() {
		filtro = new LotacaoFilter();
		pesquisar();
	}
	
	public void inicializar() {
		listaEspecialidades = especialidades.especialidades();
		listaEstabelecimentos = estabelecimentos.estabelecimentos();
		
		if (seguranca.getUsuario() != null && !seguranca.isAtendentes()) {
			filtro.setEstabelecimento(seguranca.getUsuario().getLocalAcesso());
		}
	}
	
	public LotacaoFilter getFiltro() {
		return filtro;
	}

	public LazyDataModel<Lotacao> getModel() {
		return model;
	}

	public Vinculo getVinculo() {
		return vinculo;
	}

	public void setVinculo(Vinculo vinculo) {
		this.vinculo = vinculo;
	}

	public Lotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(Lotacao lotacao) {
		this.lotacao = lotacao;
	}
	
	public List<Especialidade> getListaEspecialidades() {
		return listaEspecialidades;
	}
	
	public TipoVinculo[] getTiposVinculo() {
		return TipoVinculo.values();
	}
	
	public CargaHorariaSemanal[] getHorarios() {
		return CargaHorariaSemanal.values();
	}
	
	public StatusLotacao[] getStatuses() {
		return StatusLotacao.values();
	}


	public List<Estabelecimento> getListaEstabelecimentos() {
		return listaEstabelecimentos;
	}

	private void pesquisar() {
		model = new LazyDataModel<Lotacao>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Lotacao> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));	
				setRowCount(lotacoes.quantidadeFiltradas(filtro));
				return lotacoes.filtradas(filtro);
			}
		};
	}

	public void excluir() {
		try {
			lotacoes.remover(lotacao);
			vinculo.getLotacoes().remove(lotacao);
			FacesUtil.addInfoMessage("Lotação " + lotacao.getEstabelecimento().getDescricao() + " excluída com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}