package pmvv.semsa.rh.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.model.Solicitacao;
import pmvv.semsa.rh.service.EdicaoSolicitacaoService;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.jsf.FacesUtil;

@Named
@RequestScoped
public class EdicaoAtendimentoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EdicaoSolicitacaoService edicaoSolicitacaoService;
	@Inject
	@EdicaoAtendimento
	private Solicitacao solicitacao;
	@Inject
	private Event<EventSolicitacaoAlterada> eventSolicitacaoAlterada;
	
	public void cancelarSolicitacao() {
		try {
			solicitacao = edicaoSolicitacaoService.cancelarSolicitacao(solicitacao);
			eventSolicitacaoAlterada.fire(new EventSolicitacaoAlterada(solicitacao));
			FacesUtil.addInfoMessage("Solicitação cancelada com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void atenderSolicitacao() {
		try {
			solicitacao = edicaoSolicitacaoService.atenderSolicitacao(solicitacao);
			eventSolicitacaoAlterada.fire(new EventSolicitacaoAlterada(solicitacao));
			FacesUtil.addInfoMessage("Solicitação atendida com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}