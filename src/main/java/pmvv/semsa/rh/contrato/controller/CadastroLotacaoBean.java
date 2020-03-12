package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.StatusLotacao;
import pmvv.semsa.rh.contrato.model.TipoVinculo;
import pmvv.semsa.rh.contrato.model.Vinculo;
import pmvv.semsa.rh.contrato.repository.Estabelecimentos;
import pmvv.semsa.rh.contrato.security.Seguranca;
import pmvv.semsa.rh.contrato.service.CadastroLotacaoService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroLotacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Seguranca seguranca;
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
		if (seguranca != null && lotacao.isNovo() && !seguranca.isAdministradores()) {
			FacesUtil.addErrorMessage("Você não tem permissão para cadastrar uma nova lotação!");
		} else {
			try {
				lotacao = cadastroLotacaoService.salvar(vinculo, lotacao);
				limpar();	
				FacesUtil.addInfoMessage("Lotação salva com sucesso!");
				FacesUtil.redirecionarPagina("pesquisa.xhtml?vinculo=" + vinculo.getId());
			} catch (NegocioException ne) {
				FacesUtil.addErrorMessage(ne.getMessage());
			}
		}
	}
	
	public String getCadastro() {
		if (lotacao.getId() == null) {
			return "Nova lotação";
		}
		
		return "Edição de lotação";
	}
}