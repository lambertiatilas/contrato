package pmvv.semsa.rh.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import pmvv.semsa.rh.model.Especialidade;
import pmvv.semsa.rh.repository.Especialidades;

@FacesConverter(forClass = Especialidade.class)
public class EspecialidadeConverter implements Converter {
	
	@Inject
	private Especialidades especialidades;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Especialidade retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = especialidades.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Especialidade especialidade = (Especialidade) value;
			return especialidade.getId() == null ? null : especialidade.getId().toString();
		}
		
		return "";
	}
}