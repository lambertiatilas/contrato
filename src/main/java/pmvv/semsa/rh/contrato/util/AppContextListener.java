package pmvv.semsa.rh.contrato.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.hibernate.validator.constraints.NotBlank;
import org.primefaces.validate.bean.BeanValidationMetadataMapper;

import pmvv.semsa.rh.contrato.validation.NotBlankClientValidationConstraint;

@WebListener
public class AppContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
		BeanValidationMetadataMapper.registerConstraintMapping(NotBlank.class, new NotBlankClientValidationConstraint());
	}
}
