package pmvv.semsa.rh.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.model.ItemSolicitacao;
import pmvv.semsa.rh.model.Lotacao;
import pmvv.semsa.rh.model.Solicitacao;
import pmvv.semsa.rh.model.StatusLotacao;
import pmvv.semsa.rh.model.Vinculo;
import pmvv.semsa.rh.repository.Vinculos;
import pmvv.semsa.rh.service.CadastroSolicitacaoService;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroAtendimentoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroSolicitacaoService cadastroSolicitacaoService;
	@Produces
	@EdicaoAtendimento
	private Solicitacao solicitacao;
	private Vinculo vinculo;
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

	public List<Vinculo> getListaVinculos() {
		return listaVinculos;
	}

	public void inicializar() {
		if (solicitacao == null) {
			limpar();
		}

		verificarVinculos();
	}
	
	private void limpar() {
		solicitacao = new Solicitacao();
	}
	
	public void salvar() {
		vinculo = new Vinculo();
		
		try {
			solicitacao = cadastroSolicitacaoService.salvar(solicitacao);
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void adicionarLotacao() {
		if (vinculo.isDisponivel()) {
			if (vinculo != null) {
				if (solicitacao.naoPermiteMaisLotacao(vinculo)) {
					FacesUtil.addErrorMessage("Não é possível adicionar mais vínculos do que foi solicitado!");
					return;
				}
				
				Lotacao lotacao = new Lotacao();
				lotacao.setEstabelecimento(solicitacao.getEstabelecimentoSolicitante());
				lotacao.setStatus(StatusLotacao.PENDENTE);
				lotacao.setVinculo(vinculo);
				lotacao.setSolicitacao(solicitacao);
				solicitacao.getLotacoes().add(lotacao);
				salvar();
			}
		} else {
			FacesUtil.addErrorMessage("Este profissional já está vinculado à outro estabelecimento!");
		}
	}
	
	public void removerLotacao(Lotacao lotacao) {
		solicitacao.getLotacoes().remove(lotacao);
		salvar();
	}
	
	private void verificarVinculos() {
		listaVinculos.clear();
		
		for (ItemSolicitacao item : solicitacao.getItens()) {
			listaVinculos.addAll(vinculos.vinculosEncontrados(item));
		}
	}
	
	public void solicitacaoAlterada(@Observes EventSolicitacaoAlterada event) {
		solicitacao = event.getSolicitacao();
	}
}