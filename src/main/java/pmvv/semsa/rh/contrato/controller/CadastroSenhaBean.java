package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.security.Seguranca;
import pmvv.semsa.rh.contrato.service.CadastroUsuarioService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroSenhaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Seguranca seguranca;
	
	@Inject
	private CadastroUsuarioService cadastroUsuarioService;

	private Profissional profissional;
	private String senhaAtual;
	private String senhaNova;

	public void inicializar() {
		if (seguranca.getUsuario() != null) {
			setProfissional(seguranca.getUsuario());
		}
	}

	public void salvar() {
		try {
			profissional.conferirSenhas(senhaAtual, senhaNova);
			profissional = cadastroUsuarioService.salvar(profissional);
			FacesUtil.addInfoMessage("Senha alterada com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}
	
	@NotBlank
	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	@NotBlank
	@Size(min = 8, max = 20)
	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}
}