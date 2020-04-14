package pmvv.semsa.rh.model;

public enum StatusSolicitacao {
	
	NAO_ENVIADA("Não enviada"),
	ENVIADA("Enviada"),
	AUTORIZADA("Autorizada"),
	ATENDIDA("Atendida"),
	FINALIZADA("Finalizada");

	private String descricao;

	StatusSolicitacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}