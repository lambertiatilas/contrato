package pmvv.semsa.rh.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import pmvv.semsa.rh.util.date.DateUtil;

@Entity
@Table(name = "vinculo")
public class Vinculo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long matricula;
	private Date dataInicio;
	private Date dataFim;
	private Especialidade especialidade;
	private CargaHorariaSemanal cargaHoraria;
	private TipoVinculo tipo;
	private List<Lotacao> lotacoes = new ArrayList<>();
	private Status status  = Status.ATIVO;
	private Profissional profissional;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@NotNull
	@Min(value = 1000)
	@Max(value = 99999999)
	@Column(nullable = false, unique = true)
	public Long getMatricula() {
		return matricula;
	}

	public void setMatricula(Long matricula) {
		this.matricula = matricula;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio", nullable = false)
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_fim")
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "especialidade_id", nullable = false)
	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}

	@NotNull
	@Enumerated
	@Column(name = "carga_horaria_semanal", nullable = false)
	public CargaHorariaSemanal getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(CargaHorariaSemanal cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	@Enumerated
	@Column(nullable = false)
	public TipoVinculo getTipo() {
		return tipo;
	}

	public void setTipo(TipoVinculo tipo) {
		this.tipo = tipo;
	}

	@OneToMany(mappedBy = "vinculo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<Lotacao> getLotacoes() {
		return lotacoes;
	}

	public void setLotacoes(List<Lotacao> lotacoes) {
		this.lotacoes = lotacoes;
	}

	@Enumerated
	@Column(nullable = false)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@ManyToOne
	@JoinColumn(name = "profissional_id", nullable = false)
	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vinculo other = (Vinculo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Transient
	public boolean isNovo() {
		return id == null;
	}
	
	@Transient
	public boolean isExistente() {
		return !isNovo();
	}
	
	@Transient
	public boolean isAtivo() {
		return Status.ATIVO.equals(status);
	}
	
	@Transient
	public boolean isInativo() {
		return Status.INATIVO.equals(status);
	}
	
	@Transient
	public boolean isEfetivo() {
		return TipoVinculo.CEDIDOS.equals(tipo) || TipoVinculo.ESTATUARIO_RGPS.equals(tipo) || TipoVinculo.ESTATUARIO_RPPS.equals(tipo);
	}
	
	@Transient
	public boolean isNaoEfetivo() {
		return !isEfetivo();
	}
	
	@Transient
	public List<Lotacao> getLotacoesIniciadas() {
		List<Lotacao> lotacoes = new ArrayList<>();
		
		for (Lotacao lotacao : this.lotacoes) {
			if (lotacao.getDataInicio() != null) {
				lotacoes.add(lotacao);
			}
		}
		
 		return lotacoes;
	}
	
	public void inativarLotacoes() {
		for (Lotacao lotacao : lotacoes) {
			lotacao.setStatus(StatusLotacao.INATIVO);
			lotacao.setDataFim(new Date());
		}
	}
	
	@Transient
	public boolean isDisponivel() {
		for (Lotacao lotacao : lotacoes) {
			if (lotacao.isVinculada()) {
				return false;
			}
		}
		
		return true;
	}
	
	@Transient
	public boolean isNaoSalvarEfetivoAtivo() {
		return isEfetivo() && isAtivo() &&  dataFim != null;
	}
	
	@Transient
	public boolean isNaoSalvarNaoEfetivoAtivo() {
		return isNaoEfetivo() && isAtivo() &&  dataFim == null;
	}
	
	@Transient
	public String getVinculoFimProximo() {
		if (dataFim != null && dataFim.before(DateUtil.maisDias(31))) {
			return "vinculo-fim-proximo";
		}
		
		return "";
	}
}
