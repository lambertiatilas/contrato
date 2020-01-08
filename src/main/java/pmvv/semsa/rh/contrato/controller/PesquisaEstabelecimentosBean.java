package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
	private List<Estabelecimento> estabelecimentosFiltrados;
	
	private Estabelecimento estabelecimentoSelecionado;
	
	public PesquisaEstabelecimentosBean() {
		filtro = new EstabelecimentoFilter();
	}

	public EstabelecimentoFilter getFiltro() {
		return filtro;
	}

	public List<Estabelecimento> getEstabelecimentosFiltrados() {
		return estabelecimentosFiltrados;
	}
	
	public Estabelecimento getEstabelecimentoSelecionado() {
		return estabelecimentoSelecionado;
	}

	public void setEstabelecimentoSelecionado(Estabelecimento estabelecimentoSelecionado) {
		this.estabelecimentoSelecionado = estabelecimentoSelecionado;
	}

	public void pesquisar() {
		estabelecimentosFiltrados = estabelecimentos.filtrados(filtro);
	}
	
	public void excluir() {
		try {
			estabelecimentos.remover(estabelecimentoSelecionado);
			estabelecimentosFiltrados.remove(estabelecimentoSelecionado);
			FacesUtil.addInfoMessage("Estabelecimento " + estabelecimentoSelecionado.getDescricao() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}