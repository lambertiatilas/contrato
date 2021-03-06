package pmvv.semsa.rh.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import pmvv.semsa.rh.model.ItemSolicitacao;
import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.model.Status;
import pmvv.semsa.rh.model.Vinculo;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.jpa.Transactional;

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
	
	public Vinculo porMatricula(Long matricula) {
		try {
			return manager.createQuery("from Vinculo where matricula = :matricula", Vinculo.class).setParameter("matricula", matricula).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Long quantidadeVinculosAtivos(Profissional profissioal) {
		try {
			return manager.createQuery("select count(vinculo) from Vinculo vinculo"
				+ " where status = :status"
				+ " and profissional = :profissional"
			, Long.class)
			.setParameter("status", Status.ATIVO)
			.setParameter("profissional", profissioal)
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Vinculo> vinculosEncontrados(ItemSolicitacao item) {
		return manager.createQuery("select vinculo from Vinculo vinculo inner join vinculo.profissional profissional"
				+ " where vinculo.status = :status"
				+ " and (vinculo.dataFim is null or vinculo.dataFim > :hoje)"
				+ " and vinculo.especialidade = :especialidade"
				+ " and vinculo.cargaHoraria = :cargaHoraria"
				+ " order by profissional.nome"
			, Vinculo.class)
			.setParameter("status", Status.ATIVO)
			.setParameter("hoje", new Date())
			.setParameter("especialidade", item.getEspecialidade())
			.setParameter("cargaHoraria", item.getCargaHoraria())
			.getResultList();
	}
}