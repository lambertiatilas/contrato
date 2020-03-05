package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.service.EdicaoLotacaoService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@RequestScoped
public class EdicaoLotacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EdicaoLotacaoService edicaoLotacaoService;
	@Inject
	@Edicao
	private Lotacao lotacao;
	@Inject
	private Event<EventLotacaoAlterada> eventLotacaoAlterada;
	
	public void aceitarLotacao() {
		try {
			lotacao = edicaoLotacaoService.aceitarLotacao(lotacao);
			eventLotacaoAlterada.fire(new EventLotacaoAlterada(lotacao));
			FacesUtil.addInfoMessage("Lotação aceita com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void rejeitarLotacao() {
		try {
			lotacao = edicaoLotacaoService.rejeitarLotacao(lotacao);
			eventLotacaoAlterada.fire(new EventLotacaoAlterada(lotacao));
			FacesUtil.addInfoMessage("Lotação rejeitada com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}