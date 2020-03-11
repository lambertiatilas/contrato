package pmvv.semsa.rh.contrato.repository.filter;

import java.io.Serializable;

import pmvv.semsa.rh.contrato.model.CargaHorariaSemanal;
import pmvv.semsa.rh.contrato.model.Especialidade;
import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.model.StatusLotacao;
import pmvv.semsa.rh.contrato.model.TipoVinculo;

public class LotacaoFilter extends OrdenacaoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cpf;
	private String nome;
	private Long matricula;
	private Especialidade especialidade;
	private TipoVinculo tipoVinculo;
	private CargaHorariaSemanal cargaHoraria;
	private Estabelecimento estabelecimento;
	private StatusLotacao status = StatusLotacao.ATIVO;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getMatricula() {
		return matricula;
	}

	public void setMatricula(Long matricula) {
		this.matricula = matricula;
	}

	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}

	public TipoVinculo getTipoVinculo() {
		return tipoVinculo;
	}

	public void setTipoVinculo(TipoVinculo tipoVinculo) {
		this.tipoVinculo = tipoVinculo;
	}

	public CargaHorariaSemanal getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(CargaHorariaSemanal cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public Estabelecimento getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(Estabelecimento estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	public StatusLotacao getStatus() {
		return status;
	}

	public void setStatus(StatusLotacao status) {
		this.status = status;
	}
}