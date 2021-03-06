package pmvv.semsa.rh.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.model.Estabelecimento;
import pmvv.semsa.rh.model.Lotacao;
import pmvv.semsa.rh.model.StatusLotacao;
import pmvv.semsa.rh.model.TipoVinculo;
import pmvv.semsa.rh.model.Vinculo;
import pmvv.semsa.rh.repository.Estabelecimentos;
import pmvv.semsa.rh.service.CadastroLotacaoService;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroLotacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroLotacaoService cadastroLotacaoService;
	private Vinculo vinculo;
	private Lotacao lotacao;
	
	@Inject
	private Estabelecimentos estabelecimentos;
	private List<Estabelecimento> listaEstabelecimentos = new ArrayList<>();
	
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
	
	public List<Estabelecimento> getListaEstabelecimentos() {
		return listaEstabelecimentos;
	}

	public TipoVinculo[] getTipos() {
		return TipoVinculo.values();
	}
	
	public StatusLotacao[] getStatuses() {
		return StatusLotacao.values();
	}

	public void inicializar() {
		if (lotacao == null) {
			limpar();
		}
		
		listaEstabelecimentos = estabelecimentos.estabelecimentos();
	}
	
	private void limpar() {
		lotacao = new Lotacao();
	}

	public void salvar() {
		try {
			lotacao = cadastroLotacaoService.salvar(vinculo, lotacao);
			limpar();	
			FacesUtil.addInfoMessage("Lotação salva com sucesso!");
			FacesUtil.redirecionarPagina("pesquisa.xhtml?vinculo=" + vinculo.getId());
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public String getCadastro() {
		if (lotacao.getId() == null) {
			return "Nova lotação";
		}
		
		return "Edição de lotação";
	}
}