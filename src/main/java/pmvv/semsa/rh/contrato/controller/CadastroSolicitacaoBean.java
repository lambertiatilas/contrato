package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Especialidade;
import pmvv.semsa.rh.contrato.model.ItemSolicitacao;
import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.model.StatusLotacao;
import pmvv.semsa.rh.contrato.repository.Especialidades;
import pmvv.semsa.rh.contrato.service.CadastroSolicitacaoService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroSolicitacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroSolicitacaoService cadastroSolicitacaoService;
	private Solicitacao solicitacao;
	private ItemSolicitacao itemSolicitacao;
	private Lotacao lotacao;
	
	@Inject
	private Especialidades especialidades;
	private List<Especialidade> listaEspecialidades = new ArrayList<>();
	
	public CadastroSolicitacaoBean() {
		limpar();
	}
	
	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public ItemSolicitacao getItemSolicitacao() {
		return itemSolicitacao;
	}

	public void setItemSolicitacao(ItemSolicitacao itemSolicitacao) {
		this.itemSolicitacao = itemSolicitacao;
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
	
	public void inicializar() {
		if (solicitacao == null) {
			limpar();
		}
		
		listaEspecialidades = especialidades.especialidades();
	}
	
	private void limpar() {
		solicitacao = new Solicitacao();
		itemSolicitacao = new ItemSolicitacao();
	}
	
	public void adicionarItemSolicitacao() {
		for (ItemSolicitacao item: solicitacao.getItens()) {
			if (item.getEspecialidade().equals(itemSolicitacao.getEspecialidade()) && item.getCargaHoraria().equals(itemSolicitacao.getCargaHoraria())) {
				FacesUtil.addErrorMessage("Item já adicionado");
				return;
			}
		}
		
		itemSolicitacao.setSolicitacao(solicitacao);
		solicitacao.getItens().add(itemSolicitacao);
		itemSolicitacao = new ItemSolicitacao();
	}
	
	public void confirmarLotacao() {
		lotacao.setStatus(StatusLotacao.ATIVO);
		salvar();
	}
	
	public void salvar() {
		try {
			solicitacao = cadastroSolicitacaoService.salvar(solicitacao);
			limpar();	
			FacesUtil.addInfoMessage("Solicitação salva com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}