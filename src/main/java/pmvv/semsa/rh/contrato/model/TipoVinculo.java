package pmvv.semsa.rh.contrato.model;

public enum TipoVinculo {
	
	EFETIVO("Efetivo"),
	CONTRATADO("Contratado"),
	COMICIONADO("Comissionado"),
	ESTAGIARIO("Estagiário");

	private String descricao;

	TipoVinculo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}