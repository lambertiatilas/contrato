package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.ItemSolicitacao;
import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.model.StatusLotacao;
import pmvv.semsa.rh.contrato.model.StatusSolicitacao;
import pmvv.semsa.rh.contrato.model.Vinculo;
import pmvv.semsa.rh.contrato.repository.Vinculos;
import pmvv.semsa.rh.contrato.service.CadastroSolicitacaoService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroAtendimentoBean implements Serializable {

private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroSolicitacaoService cadastroSolicitacaoService;
	private Solicitacao solicitacao;
	private Vinculo vinculo;
	private Lotacao lotacao;
	
	@Inject
	private Vinculos vinculos;
	private List<Vinculo> listaVinculos = new ArrayList<>();
	
	public CadastroAtendimentoBean() {
		limpar();
	}
	
	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
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

	public List<Vinculo> getListaVinculos() {
		return listaVinculos;
	}

	public void inicializar() {
		if (solicitacao == null) {
			limpar();
		}

		verificarVinculosDisponiveis();
	}
	
	private void limpar() {
		solicitacao = new Solicitacao();
		lotacao = new Lotacao();
	}
	
	public void salvar() {
		if (solicitacao.isAtendimentoSalvavel()) {
			try {
				solicitacao = cadastroSolicitacaoService.salvar(solicitacao);
				FacesUtil.addInfoMessage("Solicitação salva com sucesso!");
			} catch (NegocioException ne) {
				FacesUtil.addErrorMessage(ne.getMessage());
			}
		}
	}
	
	public void adicionarLotacao() {
		if (vinculo != null) {
			lotacao.setEstabelecimento(solicitacao.getEstabelecimentoSolcitante());
			lotacao.setStatus(StatusLotacao.PENDENTE);
			lotacao.setVinculo(vinculo);
			lotacao.setSolicitacao(solicitacao);
			solicitacao.getLotacoes().add(lotacao);
			listaVinculos.remove(vinculo);
		}
	}
	
	public void removerLotacao() {
		solicitacao.getLotacoes().remove(lotacao);
		listaVinculos.add(lotacao.getVinculo());
		vinculo = lotacao.getVinculo();
	}
	
	private void verificarVinculosDisponiveis() {
		for (ItemSolicitacao item : solicitacao.getItens()) {
			listaVinculos.addAll(vinculos.vinculosDisponiveis(item));
		}
	}
	
	public void atenderSolicitacao() {
		solicitacao.setStatus(StatusSolicitacao.ATENDIDA);
		salvar();
	}
}