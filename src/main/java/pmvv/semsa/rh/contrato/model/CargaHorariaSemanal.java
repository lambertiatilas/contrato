package pmvv.semsa.rh.contrato.model;

public enum CargaHorariaSemanal {
	
	VINTE("Vinte horas"),
	VINTE_QUATRO("Vinte e quatro horas"),
	VINTE_CINCO("Vinte e cinco horas"),
	TRINTA("Trinta horas"),
	TRINTA_SEIS("Trinta e seis horas"),
	QUARENTA("Quarenta horas"),
	QUARENTA_QUATRO("Quarenta e quatro horas");
	
	private String descricao;
	
	CargaHorariaSemanal(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}