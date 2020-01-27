package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.model.Grupo;
import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.repository.Estabelecimentos;
import pmvv.semsa.rh.contrato.repository.Grupos;
import pmvv.semsa.rh.contrato.service.CadastroProfissionalService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProfissionalBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroProfissionalService cadastroProfissionalService;
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
		listaEstabelecimentos = estabelecimentos.estabelecimentos();
		listaGrupos = grupos.grupos();
		
		if (profissional == null) {
			limpar();
		}
	}
	
	private void limpar() {
		profissional = new Profissional();
		profissional.setGrupo(getListaGrupos().get(0));
	}

	public void salvar() {
		try {
			profissional = cadastroProfissionalService.salvar(profissional);
			limpar();	
			FacesUtil.addInfoMessage("Profissional salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}