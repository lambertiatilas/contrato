package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.Vinculo;
import pmvv.semsa.rh.contrato.repository.Lotacoes;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaLotacoesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Lotacoes lotacoes;
	private Vinculo vinculo;
	private Lotacao lotacao;

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