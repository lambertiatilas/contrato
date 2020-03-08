package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.service.EdicaoSolicitacaoService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@RequestScoped
public class EdicaoSolicitacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EdicaoSolicitacaoService edicaoSolicitacaoService;
	@Inject
	@EdicaoSolicitacao
	private Solicitacao solicitacao;
	@Inject
	private Event<EventSolicitacaoAlterada> eventSolicitacaoAlterada;
	
	public void enviarSolicitacao() {
		try {
			solicitacao = edicaoSolicitacaoService.enviarSolicitacao(solicitacao);
			eventSolicitacaoAlterada.fire(new EventSolicitacaoAlterada(solicitacao));
			FacesUtil.addInfoMessage("Solicitação enviada com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}