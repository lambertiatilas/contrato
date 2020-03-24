package pmvv.semsa.rh.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.repository.Profissionais;

@FacesConverter(forClass = Profissional.class)
public class ProfissionalConverter implements Converter {
	
	@Inject
	private Profissionais profissionais;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Profissional retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = profissionais.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Profissional profissional = (Profissional) value;
			return profissional.getId() == null ? null : profissional.getId().toString();
		}
		
		return "";
	}
}