package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.model.Grupo;
import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.repository.Estabelecimentos;
import pmvv.semsa.rh.contrato.repository.Grupos;
import pmvv.semsa.rh.contrato.service.CadastroUsuarioService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroUsuarioService cadastroUsuarioService;
	@Produces
	@Edicao
	private Profissional profissional;
	@Inject
	private Estabelecimentos estabelecimentos;
	private List<Estabelecimento> listaEstabelecimentos = new ArrayList<>();
	@Inject
	private Grupos grupos;
	private List<Grupo> listaGrupos = new ArrayList<>();

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}
	
	public List<Estabelecimento> getListaEstabelecimentos() {
		return listaEstabelecimentos;
	}
	
	public List<Grupo> getListaGrupos() {
		return listaGrupos;
	}

	public void inicializar() {
		if (profissional == null) {
			limpar();
		}
		
		listaEstabelecimentos = estabelecimentos.estabelecimentos();
		listaGrupos = grupos.grupos();
	}
	
	private void limpar() {
		profissional = new Profissional();
	}

	public void salvar() {
		try {
			profissional = cadastroUsuarioService.salvar(profissional);
			limpar();	
			FacesUtil.addInfoMessage("Usuário salvo com sucesso!");
			FacesUtil.redirecionarPagina("pesquisa.xhtml");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void usuarioAlterado(@Observes EventUsuarioAlterado event) {
		profissional = event.getProfissional();
	}
	
	public String getCadastro() {
		if (profissional.getId() == null) {
			return "Novo usuário";
		}
		
		return "Edição de usuário (" +  profissional.getStatus().getDescricao() + ")";
	}
}