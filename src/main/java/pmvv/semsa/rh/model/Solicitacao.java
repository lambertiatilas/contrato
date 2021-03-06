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

@Entity
@Table(name = "solicitacao")
public class Solicitacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataHoraAbertura;
	private Date dataHoraAtualizacao;
	private Date dataHoraEncerramento;
	private Profissional profissionalSolicitante;
	private Estabelecimento estabelecimentoSolicitante;
	private Profissional profissionalAutorizante;
	private Estabelecimento estabelecimentoAutorizante;
	private Profissional profissionalAtendente;
	private Estabelecimento estabelecimentoAtendente;
	private Profissional profissionalDevolvente;
	private Estabelecimento estabelecimentoDevolvente;
	private String justificativa;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora_abertura", nullable = false)
	public Date getDataHoraAbertura() {
		return dataHoraAbertura;
	}

	public void setDataHoraAbertura(Date dataHoraAbertura) {
		this.dataHoraAbertura = dataHoraAbertura;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora_atualizacao", nullable = false)
	public Date getDataHoraAtualizacao() {
		return dataHoraAtualizacao;
	}

	public void setDataHoraAtualizacao(Date dataHoraAtualizacao) {
		this.dataHoraAtualizacao = dataHoraAtualizacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora_encerramento")
	public Date getDataHoraEncerramento() {
		return dataHoraEncerramento;
	}

	public void setDataHoraEncerramento(Date dataHoraEncerramento) {
		this.dataHoraEncerramento = dataHoraEncerramento;
	}

	@ManyToOne
	@JoinColumn(name = "profissional_solicitante_id", nullable = false)
	public Profissional getProfissionalSolicitante() {
		return profissionalSolicitante;
	}

	public void setProfissionalSolicitante(Profissional profissionalSolicitante) {
		this.profissionalSolicitante = profissionalSolicitante;
	}

	@ManyToOne
	@JoinColumn(name = "estabelecimento_solicitante_id", nullable = false)
	public Estabelecimento getEstabelecimentoSolicitante() {
		return estabelecimentoSolicitante;
	}

	public void setEstabelecimentoSolicitante(Estabelecimento estabelecimentoSolicitante) {
		this.estabelecimentoSolicitante = estabelecimentoSolicitante;
	}

	@ManyToOne
	@JoinColumn(name = "profissional_autorizante_id")
	public Profissional getProfissionalAutorizante() {
		return profissionalAutorizante;
	}

	public void setProfissionalAutorizante(Profissional profissionalAutorizante) {
		this.profissionalAutorizante = profissionalAutorizante;
	}

	@ManyToOne
	@JoinColumn(name = "estabelecimento_autorizante_id")
	public Estabelecimento getEstabelecimentoAutorizante() {
		return estabelecimentoAutorizante;
	}

	public void setEstabelecimentoAutorizante(Estabelecimento estabelecimentoAutorizante) {
		this.estabelecimentoAutorizante = estabelecimentoAutorizante;
	}

	@ManyToOne
	@JoinColumn(name = "profissional_atendente_id")
	public Profissional getProfissionalAtendente() {
		return profissionalAtendente;
	}

	public void setProfissionalAtendente(Profissional profissionalAtendente) {
		this.profissionalAtendente = profissionalAtendente;
	}

	@ManyToOne
	@JoinColumn(name = "estabelecimento_atendente_id")
	public Estabelecimento getEstabelecimentoAtendente() {
		return estabelecimentoAtendente;
	}

	public void setEstabelecimentoAtendente(Estabelecimento estabelecimentoAtendente) {
		this.estabelecimentoAtendente = estabelecimentoAtendente;
	}
	
	@ManyToOne
	@JoinColumn(name = "profissional_devolvente_id")
	public Profissional getProfissionalDevolvente() {
		return profissionalDevolvente;
	}

	public void setProfissionalDevolvente(Profissional profissionalDevolvente) {
		this.profissionalDevolvente = profissionalDevolvente;
	}

	@ManyToOne
	@JoinColumn(name = "estabelecimento_devolvente_id")
	public Estabelecimento getEstabelecimentoDevolvente() {
		return estabelecimentoDevolvente;
	}

	public void setEstabelecimentoDevolvente(Estabelecimento estabelecimentoDevolvente) {
		this.estabelecimentoDevolvente = estabelecimentoDevolvente;
	}

	@Column(columnDefinition = "text")
	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<ItemSolicitacao> getItens() {
		return itens;
	}

	public void setItens(List<ItemSolicitacao> itens) {
		this.itens = itens;
	}

	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
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

	@Transient
	private boolean isNaoEnviada() {
		return StatusSolicitacao.NAO_ENVIADA.equals(status);
	}

	@Transient
	private boolean isEnviada() {
		return StatusSolicitacao.ENVIADA.equals(status);
	}
	
	@Transient
	private boolean isAutorizada() {
		return StatusSolicitacao.AUTORIZADA.equals(status);
	}

	@Transient
	private boolean isAtendida() {
		return StatusSolicitacao.ATENDIDA.equals(status);
	}

	@Transient
	private boolean isFinalizada() {
		return StatusSolicitacao.FINALIZADA.equals(status);
	}

	@Transient
	private boolean isItemExistente() {
		return !itens.isEmpty();
	}

	@Transient
	private boolean isLotacaoExistente() {
		return !lotacoes.isEmpty();
	}

	@Transient
	public boolean isNovo() {
		return id == null;
	}

	@Transient
	public boolean isExistente() {
		return !isNovo();
	}

	public boolean isEnviavel() {
		return isExistente() && isNaoEnviada() && isItemExistente();
	}
	
	public boolean isNaoEnviavel() {
		return !isEnviavel();
	}
	
	public boolean isAutorizavel() {
		return isEnviada();
	}
	
	public boolean isNaoAutorizavel() {
		return !isAutorizavel();
	}
	
	public boolean isAtendivel() {
		return isAutorizada() && isLotacaoExistente();
	}
	
	public boolean isNaoAtendivel() {
		return !isAtendivel();
	}
	
	public boolean isDevolvivel() {
		return isEnviada() || isAutorizada();
	}
	
	public boolean isNaoDevolvivel() {
		return !isDevolvivel();
	}

	@Transient
	public boolean isLotacoesPendentes() {
		for (Lotacao lotacao : lotacoes) {
			if (StatusLotacao.PENDENTE.equals(lotacao.getStatus())) {
				return true;
			}
		}
		return false;
	}

	@Transient
	public boolean isLotacoesNaoPendentes() {
		return isLotacaoExistente() && !isLotacoesPendentes();
	}

	public boolean naoPermiteMaisLotacao(Vinculo vinculo) {
		Integer contador = 0;

		for (Lotacao lotacao : lotacoes) {
			if (lotacao.getVinculo().getEspecialidade().equals(vinculo.getEspecialidade())
					&& lotacao.getVinculo().getCargaHoraria().equals(vinculo.getCargaHoraria())) {
				contador++;
			}
		}

		for (ItemSolicitacao item : itens) {
			if (item.getEspecialidade().equals(vinculo.getEspecialidade())
					&& item.getCargaHoraria().equals(vinculo.getCargaHoraria()) && contador >= item.getQuantidade()) {
				return true;
			}
		}

		return false;
	}
}