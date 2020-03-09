package pmvv.semsa.rh.contrato.service;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.model.Status;
import pmvv.semsa.rh.contrato.model.StatusLotacao;
import pmvv.semsa.rh.contrato.model.Vinculo;
import pmvv.semsa.rh.contrato.repository.Vinculos;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class CadastroVinculoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Vinculos vinculos;
	
	@Transactional
	public Vinculo salvar(Profissional profissional, Vinculo vinculo) throws NegocioException {
		
		if (vinculo.isNovo()) {
			vinculo.setStatus(Status.ATIVO);
			vinculo.setProfissional(profissional);
		}
		
		if (vinculo.isExistente() && vinculo.getStatus().equals(Status.INATIVO)) {
			for (Lotacao lotacao : vinculo.getLotacoes()) {
				if (lotacao.getStatus().equals(StatusLotacao.ATIVO)) {
					lotacao.setStatus(StatusLotacao.INATIVO);
				}
			}
		}
		
		return vinculos.guardar(vinculo);
	}
}