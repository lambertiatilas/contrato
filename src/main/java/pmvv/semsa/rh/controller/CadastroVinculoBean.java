package pmvv.semsa.rh.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.model.CargaHorariaSemanal;
import pmvv.semsa.rh.model.Especialidade;
import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.model.Status;
import pmvv.semsa.rh.model.TipoVinculo;
import pmvv.semsa.rh.model.Vinculo;
import pmvv.semsa.rh.repository.Especialidades;
import pmvv.semsa.rh.service.CadastroVinculoService;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroVinculoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroVinculoService cadastroVinculoService;
	private Profissional profissional;
	private Vinculo vinculo;
	
	@Inject
	private Especialidades especialidades;
	private List<Especialidade> listaEspecialidades = new ArrayList<>();
	
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

	public List<Especialidade> getListaEspecialidades() {
		return listaEspecialidades;
	}
	
	public CargaHorariaSemanal[] getHorarios() {
		return CargaHorariaSemanal.values();
	}
	
	public TipoVinculo[] getTipos() {
		return TipoVinculo.values();
	}
	
	public Status[] getStatuses() {
		return Status.values();
	}

	public void inicializar() {
		if (vinculo == null) {
			limpar();
		}
		
		listaEspecialidades = especialidades.especialidades();
	}
	
	private void limpar() {
		vinculo = new Vinculo();
	}

	public void salvar() {
		try {
			vinculo = cadastroVinculoService.salvar(profissional, vinculo);
			limpar();	
			FacesUtil.addInfoMessage("Vínculo salvo com sucesso!");
			FacesUtil.redirecionarPagina("pesquisa.xhtml?profissional=" + profissional.getId());
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public String getCadastro() {
		if (vinculo.getId() == null) {
			return "Novo vínculo";
		}
		
		return "Edição de vínculo";
	}
}