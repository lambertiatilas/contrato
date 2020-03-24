package pmvv.semsa.rh.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.model.Estabelecimento;
import pmvv.semsa.rh.service.CadastroEstabelecimentoService;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroEstabelecimentoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroEstabelecimentoService cadastroEstabelecimentoService;
	private Estabelecimento estabelecimento;
	
	public Estabelecimento getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(Estabelecimento estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	public void inicializar() {
		if (estabelecimento == null) {
			limpar();
		}
	}
	
	private void limpar() {
		estabelecimento = new Estabelecimento();
	}

	public void salvar() {
		try {
			estabelecimento = cadastroEstabelecimentoService.salvar(estabelecimento);
			limpar();
			FacesUtil.addInfoMessage("Estabelecimento salvo com sucesso!");
			FacesUtil.redirecionarPagina("pesquisa.xhtml");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public String getCadastro() {
		if (estabelecimento.getId() == null) {
			return "Novo estabelecimento";
		}
		
		return "Edição de estabelecimento";
	}
}