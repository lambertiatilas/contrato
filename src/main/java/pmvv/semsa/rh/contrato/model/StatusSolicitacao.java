package pmvv.semsa.rh.contrato.model;

public enum StatusSolicitacao {
	
	NAO_ENVIADA("Não enviada"),
	ENVIADA("Enviada"),
	ENCERRADA("Encerrada");

	private String descricao;

	StatusSolicitacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}