package pmvv.semsa.rh.contrato.model;

public enum CargaHorariaSemanal {
	
	VINTE("Vinte horas semanais"),
	TRINTA("Trinta horas semanais"),
	QUARENTA("Quarenta horas semanais");
	
	private String descricao;
	
	CargaHorariaSemanal(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}