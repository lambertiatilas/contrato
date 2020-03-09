package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.CargaHorariaSemanal;
import pmvv.semsa.rh.contrato.model.Especialidade;
import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.model.Status;
import pmvv.semsa.rh.contrato.model.TipoVinculo;
import pmvv.semsa.rh.contrato.model.Vinculo;
import pmvv.semsa.rh.contrato.repository.Especialidades;
import pmvv.semsa.rh.contrato.service.CadastroVinculoService;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jsf.FacesUtil;

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
}