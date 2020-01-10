package pmvv.semsa.rh.contrato.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class Lotacoes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public Lotacao guardar(Lotacao lotacao) {
		return manager.merge(lotacao);
	}
	
	@Transactional
	public void remover(Lotacao lotacao) throws NegocioException {
		try {
			lotacao = porId(lotacao.getId());
			manager.remove(lotacao);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Lotação não pode ser excluída.");
		}
	}
	
	public Lotacao porId(Long id) {
		return manager.find(Lotacao.class, id);
	}
}