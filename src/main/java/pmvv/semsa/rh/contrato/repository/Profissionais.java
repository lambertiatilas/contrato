package pmvv.semsa.rh.contrato.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.repository.filter.ProfissionalFilter;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class Profissionais implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public Profissional guardar(Profissional profissional) {
		return manager.merge(profissional);
	}
	
	@Transactional
	public void remover(Profissional profissional) throws NegocioException {
		try {
			profissional = porId(profissional.getId());
			manager.remove(profissional);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Profissional não pode ser excluído.");
		}
	}
	
	public Profissional porId(Long id) {
		return manager.find(Profissional.class, id);
	}

	public Profissional porNome(String nome) {
		return this.manager.createQuery("from Profissional where upper(nome) = :nome", Profissional.class).setParameter("nome", nome.toUpperCase()).getSingleResult();
	}
	
	public List<Profissional> filtrados(ProfissionalFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Profissional> criteriaQuery = builder.createQuery(Profissional.class);
		List<Predicate> predicates = new ArrayList<>();
		
		Root<Profissional> profissionalRoot = criteriaQuery.from(Profissional.class);
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(profissionalRoot.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getCpf())) {
			predicates.add(builder.like(builder.upper(profissionalRoot.get("cpf")), "%" + filtro.getCpf().toUpperCase() + "%"));
		}
		
		criteriaQuery.select(profissionalRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(profissionalRoot.get("nome")));
		
		TypedQuery<Profissional> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
}