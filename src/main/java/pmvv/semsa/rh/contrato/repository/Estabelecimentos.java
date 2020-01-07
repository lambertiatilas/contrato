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

import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.repository.filter.EstabelecimentoFilter;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class Estabelecimentos implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public Estabelecimento guardar(Estabelecimento estabelecimento) {
		return manager.merge(estabelecimento);
	}
	
	@Transactional
	public void remover(Estabelecimento estabelecimento) throws NegocioException {
		try {
			estabelecimento = porId(estabelecimento.getId());
			manager.remove(estabelecimento);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Estabelecimento não pode ser excluído.");
		}
	}
	
	public Estabelecimento porId(Long id) {
		return manager.find(Estabelecimento.class, id);
	}

	public Estabelecimento porDescricao(String descricao) {
		try {
			return manager.createQuery("from Estabelecimento where upper(descricao) = :descricao", Estabelecimento.class).setParameter("descricao", descricao.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Estabelecimento> filtrados(EstabelecimentoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Estabelecimento> criteriaQuery = builder.createQuery(Estabelecimento.class);
		List<Predicate> predicates = new ArrayList<>();
		
		Root<Estabelecimento> estabelecimentoRoot = criteriaQuery.from(Estabelecimento.class);
		
		if (StringUtils.isNotBlank(filtro.getDescricao())) {
			predicates.add(builder.like(builder.upper(estabelecimentoRoot.get("descricao")), "%" + filtro.getDescricao().toUpperCase() + "%"));
		}
		
		criteriaQuery.select(estabelecimentoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(estabelecimentoRoot.get("descricao")));
		
		TypedQuery<Estabelecimento> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
}