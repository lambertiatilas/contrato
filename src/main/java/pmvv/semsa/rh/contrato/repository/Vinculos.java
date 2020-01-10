package pmvv.semsa.rh.contrato.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import pmvv.semsa.rh.contrato.model.Vinculo;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class Vinculos implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public Vinculo guardar(Vinculo vinculo) {
		return manager.merge(vinculo);
	}
	
	@Transactional
	public void remover(Vinculo vinculo) throws NegocioException {
		try {
			vinculo = porId(vinculo.getId());
			manager.remove(vinculo);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Vínculo não pode ser excluído.");
		}
	}
	
	public Vinculo porId(Long id) {
		return manager.find(Vinculo.class, id);
	}
}