package pmvv.semsa.rh.contrato.model;

public enum CargaHorariaSemanal {
	
	VINTE("Vinte horas"),
	TRINTA("Trinta horas"),
	QUARENTA("Quarenta horas");
	
	private String descricao;
	
	CargaHorariaSemanal(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}