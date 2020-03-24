package pmvv.semsa.rh.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import pmvv.semsa.rh.model.Solicitacao;
import pmvv.semsa.rh.repository.Solicitacoes;

@FacesConverter(forClass = Solicitacao.class)
public class SolicitacaoConverter implements Converter {
	
	@Inject
	private Solicitacoes solicitacoes;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Solicitacao retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = solicitacoes.porId(id);
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