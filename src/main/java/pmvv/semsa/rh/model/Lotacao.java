package pmvv.semsa.rh.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "lotacao")
public class Lotacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataInicio;
	private Date dataFim;
	private Estabelecimento estabelecimento;
	private StatusLotacao status = StatusLotacao.ATIVO;
	private Vinculo vinculo;
	private Solicitacao solicitacao;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio")
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
	@JoinColumn(name = "estabelecimento_id", nullable = false)
	public Estabelecimento getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(Estabelecimento estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	@Enumerated
	@Column(nullable = false)
	public StatusLotacao getStatus() {
		return status;
	}

	public void setStatus(StatusLotacao status) {
		this.status = status;
	}
	
	@ManyToOne
	@JoinColumn(name = "vinculo_id", nullable = false)
	public Vinculo getVinculo() {
		return vinculo;
	}

	public void setVinculo(Vinculo vinculo) {
		this.vinculo = vinculo;
	}
	
	@ManyToOne
	@JoinColumn(name = "solicitacao_id")
	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
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
		Lotacao other = (Lotacao) obj;
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
		return StatusLotacao.ATIVO.equals(status);
	}
	
	@Transient
	private boolean isPendente() {
		return StatusLotacao.PENDENTE.equals(status);
	}
	
	@Transient
	public boolean isInativo() {
		return StatusLotacao.INATIVO.equals(status);
	}
	
	@Transient
	public boolean isAceitavelOuRejeitavel() {
		return isExistente() && isPendente();
	}
	
	@Transient
	public boolean isNaoAceitavelOuNaoRejeitavel() {
		return !isAceitavelOuRejeitavel();
	}
	
	@Transient
	public boolean isVinculada() {
		return isPendente() || isAtivo();
	}
}