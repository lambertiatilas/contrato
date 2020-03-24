package pmvv.semsa.rh.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import pmvv.semsa.rh.model.Solicitacao;
import pmvv.semsa.rh.repository.Solicitacoes;
import pmvv.semsa.rh.security.Seguranca;

@FacesConverter(forClass = Solicitacao.class)
public class SolicitacaoPorEstabelecimentoConverter implements Converter {
	
	@Inject
	private Seguranca seguranca;
	@Inject
	private Solicitacoes solicitacoes;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Solicitacao retorno = null;
		
		if (seguranca.getUsuario() == null) {
			return null;
		}
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = solicitacoes.porEstabelecimentoSolicitante(seguranca.getUsuario().getLocalAcesso(), id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Solicitacao solicitacao = (Solicitacao) value;
			return solicitacao.getId() == null ? null : solicitacao.getId().toString();
		}
		
		return "";
	}
}