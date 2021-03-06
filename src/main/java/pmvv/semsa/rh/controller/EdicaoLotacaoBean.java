package pmvv.semsa.rh.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.model.Lotacao;
import pmvv.semsa.rh.service.EdicaoLotacaoService;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.jsf.FacesUtil;

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
	
	public void aceitar() {
		try {
			lotacao = edicaoLotacaoService.aceitar(lotacao);
			eventLotacaoAlterada.fire(new EventLotacaoAlterada(lotacao));
			FacesUtil.addInfoMessage("Lotação aceita com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void rejeitar() {
		try {
			lotacao = edicaoLotacaoService.rejeitar(lotacao);
			eventLotacaoAlterada.fire(new EventLotacaoAlterada(lotacao));
			FacesUtil.addInfoMessage("Lotação rejeitada com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}