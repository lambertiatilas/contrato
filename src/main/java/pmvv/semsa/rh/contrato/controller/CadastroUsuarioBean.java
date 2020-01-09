package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Especialidade;
import pmvv.semsa.rh.contrato.model.Grupo;
import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.model.Status;
import pmvv.semsa.rh.contrato.model.TipoVinculo;
import pmvv.semsa.rh.contrato.model.Vinculo;
import pmvv.semsa.rh.contrato.repository.Especialidades;
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
	private Profissional profissional;
	private Vinculo vinculo;
	private Lotacao lotacao;
	
	@Inject
	private Especialidades especialidades;
	private List<Especialidade> listaEspecialidades = new ArrayList<>();
	
	@Inject
	private Grupos grupos;
	private List<Grupo> listaGrupos = new ArrayList<>();
	
	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}
	
	public Vinculo getVinculo() {
		return vinculo;
	}

	public void setVinculo(Vinculo vinculo) {
		this.vinculo = vinculo;
	}

	public Lotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(Lotacao lotacao) {
		this.lotacao = lotacao;
	}

	public List<Especialidade> getListaEspecialidades() {
		return listaEspecialidades;
	}
	
	public TipoVinculo[] getTipos() {
		return TipoVinculo.values();
	}

	public List<Grupo> getListaGrupos() {
		return listaGrupos;
	}
	
	public Status[] getStatuses() {
		return Status.values();
	}

	public void inicializar() {
		if (profissional == null) {
			limpar();
		}
		
		listaEspecialidades = especialidades.especialidades();
		listaGrupos = grupos.grupos();
	}
	
	private void limpar() {
		profissional = new Profissional();
		vinculo = new Vinculo();
		lotacao = new Lotacao();
	}

	public void salvar() {
		try {
			profissional = cadastroUsuarioService.salvar(profissional);
			limpar();	
			FacesUtil.addInfoMessage("Usuário salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}