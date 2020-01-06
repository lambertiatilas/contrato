package pmvv.semsa.rh.contrato.model;

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
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "solicitacao")
public class Solicitacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataHoraAbertura;
	private Date dataHoraEncerramento;
	private Profissional profissionalSolicitante;
	private Estabelecimento estabelecimentoSolcitante;
	private Profissional profissionalAtendente;
	private Estabelecimento estabelecimentoAtendente;
	private List<ItemSolicitacao> itens = new ArrayList<>();
	private List<Lotacao> lotacoes = new ArrayList<>();
	private StatusSolicitacao status;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora_abertura", nullable = false)
	public Date getDataHoraAbertura() {
		return dataHoraAbertura;
	}

	public void setDataHoraAbertura(Date dataHoraAbertura) {
		this.dataHoraAbertura = dataHoraAbertura;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora_encerramento", nullable = false)
	public Date getDataHoraEncerramento() {
		return dataHoraEncerramento;
	}

	public void setDataHoraEncerramento(Date dataHoraEncerramento) {
		this.dataHoraEncerramento = dataHoraEncerramento;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "profissional_solicitante_id", nullable = false)
	public Profissional getProfissionalSolicitante() {
		return profissionalSolicitante;
	}

	public void setProfissionalSolicitante(Profissional profissionalSolicitante) {
		this.profissionalSolicitante = profissionalSolicitante;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "estabelecimento_solicitante_id", nullable = false)
	public Estabelecimento getEstabelecimentoSolcitante() {
		return estabelecimentoSolcitante;
	}

	public void setEstabelecimentoSolcitante(Estabelecimento estabelecimentoSolcitante) {
		this.estabelecimentoSolcitante = estabelecimentoSolcitante;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "profissional_atendente_id", nullable = false)
	public Profissional getProfissionalAtendente() {
		return profissionalAtendente;
	}

	public void setProfissionalAtendente(Profissional profissionalAtendente) {
		this.profissionalAtendente = profissionalAtendente;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "estabelecimento_atendente_id", nullable = false)
	public Estabelecimento getEstabelecimentoAtendente() {
		return estabelecimentoAtendente;
	}

	public void setEstabelecimentoAtendente(Estabelecimento estabelecimentoAtendente) {
		this.estabelecimentoAtendente = estabelecimentoAtendente;
	}

	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<ItemSolicitacao> getItens() {
		return itens;
	}

	public void setItens(List<ItemSolicitacao> itens) {
		this.itens = itens;
	}

	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<Lotacao> getLotacoes() {
		return lotacoes;
	}

	public void setLotacoes(List<Lotacao> lotacoes) {
		this.lotacoes = lotacoes;
	}

	@Enumerated
	@Column(nullable = false)
	public StatusSolicitacao getStatus() {
		return status;
	}

	public void setStatus(StatusSolicitacao status) {
		this.status = status;
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
		Solicitacao other = (Solicitacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}