package pmvv.semsa.rh.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import pmvv.semsa.rh.model.Vinculo;
import pmvv.semsa.rh.repository.Vinculos;

@FacesConverter(forClass = Vinculo.class)
public class VinculoConverter implements Converter {
	
	@Inject
	private Vinculos vinculos;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Vinculo retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = vinculos.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Vinculo vinculo = (Vinculo) value;
			return vinculo.getId() == null ? null : vinculo.getId().toString();
		}
		
		return "";
	}
}