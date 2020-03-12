package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Especialidade;
import pmvv.semsa.rh.contrato.service.CadastroEspecialidadeService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroEspecialidadeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroEspecialidadeService cadastroEspecialidadeService;
	private Especialidade especialidade;
	
	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}

	public void inicializar() {
		if (especialidade == null) {
			limpar();
		}
	}
	
	private void limpar() {
		especialidade = new Especialidade();
	}

	public void salvar() {
		try {
			especialidade = cadastroEspecialidadeService.salvar(especialidade);
			limpar();	
			FacesUtil.addInfoMessage("Especialidade salva com sucesso!");
			FacesUtil.redirecionarPagina("pesquisa.xhtml");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public String getCadastro() {
		if (especialidade.getId() == null) {
			return "Novo cargo";
		}
		
		return "Edição de cargo";
	}
}