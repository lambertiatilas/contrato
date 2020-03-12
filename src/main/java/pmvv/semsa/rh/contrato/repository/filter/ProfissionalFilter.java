package pmvv.semsa.rh.contrato.repository.filter;

import java.io.Serializable;

import pmvv.semsa.rh.contrato.model.Status;

public class ProfissionalFilter extends OrdenacaoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String cpf;
	private Status status;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}