package pmvv.semsa.rh.contrato.model;

public enum StatusLotacao {
	
	ATIVO("Ativo"),
	PENDENTE("Pendente"),
	INATIVO("Inativo");
	
	private String descricao;
	
	StatusLotacao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}