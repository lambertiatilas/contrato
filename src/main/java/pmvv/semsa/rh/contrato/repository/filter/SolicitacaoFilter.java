package pmvv.semsa.rh.contrato.repository.filter;

import java.io.Serializable;
import java.util.Date;

import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.model.StatusSolicitacao;

public class SolicitacaoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataHoraAberturaDe;
	private Date dataHoraAberturaAte;
	private Date dataHoraEncerramentoDe;
	private Date dataHoraEncerramentoAte;
	private String profissionalSolicitante;
	private Estabelecimento estabelecimentoSolicitante;
	private String profissionalAtendente;
	private Estabelecimento estabelecimentoAtendente;
	private StatusSolicitacao status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataHoraAberturaDe() {
		return dataHoraAberturaDe;
	}

	public void setDataHoraAberturaDe(Date dataHoraAberturaDe) {
		this.dataHoraAberturaDe = dataHoraAberturaDe;
	}

	public Date getDataHoraAberturaAte() {
		return dataHoraAberturaAte;
	}

	public void setDataHoraAberturaAte(Date dataHoraAberturaAte) {
		this.dataHoraAberturaAte = dataHoraAberturaAte;
	}

	public Date getDataHoraEncerramentoDe() {
		return dataHoraEncerramentoDe;
	}

	public void setDataHoraEncerramentoDe(Date dataHoraEncerramentoDe) {
		this.dataHoraEncerramentoDe = dataHoraEncerramentoDe;
	}

	public Date getDataHoraEncerramentoAte() {
		return dataHoraEncerramentoAte;
	}

	public void setDataHoraEncerramentoAte(Date dataHoraEncerramentoAte) {
		this.dataHoraEncerramentoAte = dataHoraEncerramentoAte;
	}

	public String getProfissionalSolicitante() {
		return profissionalSolicitante;
	}

	public void setProfissionalSolicitante(String profissionalSolicitante) {
		this.profissionalSolicitante = profissionalSolicitante;
	}

	public Estabelecimento getEstabelecimentoSolicitante() {
		return estabelecimentoSolicitante;
	}

	public void setEstabelecimentoSolicitante(Estabelecimento estabelecimentoSolicitante) {
		this.estabelecimentoSolicitante = estabelecimentoSolicitante;
	}

	public String getProfissionalAtendente() {
		return profissionalAtendente;
	}

	public void setProfissionalAtendente(String profissionalAtendente) {
		this.profissionalAtendente = profissionalAtendente;
	}

	public Estabelecimento getEstabelecimentoAtendente() {
		return estabelecimentoAtendente;
	}

	public void setEstabelecimentoAtendente(Estabelecimento estabelecimentoAtendente) {
		this.estabelecimentoAtendente = estabelecimentoAtendente;
	}

	public StatusSolicitacao getStatus() {
		return status;
	}

	public void setStatus(StatusSolicitacao status) {
		this.status = status;
	}
}