package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.model.ItemSolicitacao;
import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.model.StatusLotacao;
import pmvv.semsa.rh.contrato.model.Vinculo;
import pmvv.semsa.rh.contrato.repository.Estabelecimentos;
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
	private Lotacao lotacao;
	
	@Inject
	private Vinculos vinculos;
	private List<Vinculo> listaVinculos = new ArrayList<>();
	
	@Inject
	private Estabelecimentos estabelecimentos;
	private List<Estabelecimento> listaEstabelecimentos = new ArrayList<>();
	
	public CadastroAtendimentoBean() {
		limpar();
	}
	
	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
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

	public List<Estabelecimento> getListaEstabelecimentos() {
		return listaEstabelecimentos;
	}

	public void inicializar() {
		if (solicitacao == null) {
			limpar();
		}

		listaEstabelecimentos = estabelecimentos.estabelecimentos();
		verificarVinculosDisponiveis();
	}
	
	private void limpar() {
		solicitacao = new Solicitacao();
		lotacao = new Lotacao();
	}
	
	public void salvar() {
		try {
			solicitacao = cadastroSolicitacaoService.salvar(solicitacao);
			FacesUtil.addInfoMessage("Solicitação salva com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void adicionarLotacao() {
		lotacao.setDataInicio(new Date());
		lotacao.setEstabelecimento(solicitacao.getEstabelecimentoSolcitante());
		lotacao.setStatus(StatusLotacao.PENDENTE);
		lotacao.setSolicitacao(solicitacao);
		solicitacao.getLotacoes().add(lotacao);
	}
	
	private void verificarVinculosDisponiveis() {
		for (ItemSolicitacao item : solicitacao.getItens()) {
			listaVinculos.addAll(vinculos.vinculosDisponiveis(item));
		}
	}
}