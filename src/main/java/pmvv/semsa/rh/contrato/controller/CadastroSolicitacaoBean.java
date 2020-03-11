package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.CargaHorariaSemanal;
import pmvv.semsa.rh.contrato.model.Especialidade;
import pmvv.semsa.rh.contrato.model.ItemSolicitacao;
import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.Solicitacao;
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
	@Produces
	@EdicaoSolicitacao
	private Solicitacao solicitacao;
	private ItemSolicitacao itemSolicitacao;
	@Produces
	@Edicao
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
	
	public CargaHorariaSemanal[] getHorarios() {
		return CargaHorariaSemanal.values();
	}
	
	public void inicializar() {
		if (solicitacao == null) {
			limpar();
		}
		
		listaEspecialidades = especialidades.especialidades();
	}
	
	private void limpar() {
		solicitacao = new Solicitacao();
		limparItemSolicitacao();
	}
	
	private void limparItemSolicitacao() {
		itemSolicitacao = new ItemSolicitacao();
	}
	
	public void adicionarItemSolicitacao() {
		if (itemSolicitacao.getEspecialidade() == null || itemSolicitacao.getCargaHoraria() == null || itemSolicitacao.getQuantidade() == null) {
			FacesUtil.addErrorMessage("Todos os campos da solicitação devem ser preenchidos!");
			return;
		}
		
		for (ItemSolicitacao item: solicitacao.getItens()) {
			if (item.getEspecialidade().equals(itemSolicitacao.getEspecialidade()) && item.getCargaHoraria().equals(itemSolicitacao.getCargaHoraria())) {
				FacesUtil.addErrorMessage("Já existe um cargo de " + item.getEspecialidade().getDescricao() + " de " + item.getCargaHoraria().getDescricao() + " nesta solicitação!");
				return;
			}
		}
		
		itemSolicitacao.setSolicitacao(solicitacao);
		solicitacao.getItens().add(itemSolicitacao);
		limparItemSolicitacao();
		salvar();
	}
	
	public void removerItemSolicitacao() {
		solicitacao.getItens().remove(itemSolicitacao);
		salvar();
	}
	
	private void salvar() {
		try {
			solicitacao = cadastroSolicitacaoService.salvar(solicitacao);	
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void solicitacaoAlterada(@Observes EventSolicitacaoAlterada event) {
		solicitacao = event.getSolicitacao();
	}
	
	public void lotacaoAlterada(@Observes EventLotacaoAlterada event) {
		solicitacao = event.getLotacao().getSolicitacao();
	}
}