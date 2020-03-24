package pmvv.semsa.rh.model;

public enum TipoVinculo {
	
	ACT("ACT- Temporário"),
	BOLSISTA("Bolsista"),
	CEDIDOS("Cedidos"),
	CLT("CLT"),
	ESTAGIARIO("Estagiário"),
	ESTATUARIO_RGPS("Estatuário - RGPS"),
	ESTATUARIO_RPPS("Estatuário - RPPS");

	private String descricao;

	TipoVinculo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}