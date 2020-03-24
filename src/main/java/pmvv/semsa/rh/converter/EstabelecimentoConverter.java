package pmvv.semsa.rh.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import pmvv.semsa.rh.model.Estabelecimento;
import pmvv.semsa.rh.repository.Estabelecimentos;

@FacesConverter(forClass = Estabelecimento.class)
public class EstabelecimentoConverter implements Converter {
	
	@Inject
	private Estabelecimentos estabelecimentos;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Estabelecimento retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = estabelecimentos.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Estabelecimento estabelecimento = (Estabelecimento) value;
			return estabelecimento.getId() == null ? null : estabelecimento.getId().toString();
		}
		
		return "";
	}
}