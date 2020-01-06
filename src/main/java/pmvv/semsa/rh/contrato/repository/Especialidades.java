package pmvv.semsa.rh.contrato.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import pmvv.semsa.rh.contrato.model.Especialidade;
import pmvv.semsa.rh.contrato.repository.filter.EspecialidadeFilter;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class Especialidades implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public Especialidade guardar(Especialidade especialidade) {
		return manager.merge(especialidade);
	}
	
	@Transactional
	public void remover(Especialidade especialidade) throws NegocioException {
		try {
			especialidade = porId(especialidade.getId());
			manager.remove(especialidade);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Especialidade não pode ser excluída.");
		}
	}
	
	public Especialidade porId(Long id) {
		return manager.find(Especialidade.class, id);
	}

	public Especialidade porDescricao(String descricao) {
		try {
			return manager.createQuery("from Especialidade where upper(descricao) = :descricao", Especialidade.class).setParameter("descricao", descricao.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Especialidade> filtradas(EspecialidadeFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Especialidade> criteriaQuery = builder.createQuery(Especialidade.class);
		List<Predicate> predicates = new ArrayList<>();
		
		Root<Especialidade> especialidadeRoot = criteriaQuery.from(Especialidade.class);
		
		if (StringUtils.isNotBlank(filtro.getDescricao())) {
			predicates.add(builder.like(builder.upper(especialidadeRoot.get("descricao")), "%" + filtro.getDescricao().toUpperCase() + "%"));
		}
		
		criteriaQuery.select(especialidadeRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(especialidadeRoot.get("descricao")));
		
		TypedQuery<Especialidade> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
}