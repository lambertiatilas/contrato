package pmvv.semsa.rh.contrato.repository.filter;

import java.io.Serializable;

public class EspecialidadeFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}