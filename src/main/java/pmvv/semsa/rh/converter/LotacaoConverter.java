package pmvv.semsa.rh.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import pmvv.semsa.rh.model.Lotacao;
import pmvv.semsa.rh.repository.Lotacoes;

@FacesConverter(forClass = Lotacao.class)
public class LotacaoConverter implements Converter {
	
	@Inject
	private Lotacoes lotacoes;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Lotacao retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = lotacoes.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Lotacao lotacao = (Lotacao) value;
			return lotacao.getId() == null ? null : lotacao.getId().toString();
		}
		
		return "";
	}
}