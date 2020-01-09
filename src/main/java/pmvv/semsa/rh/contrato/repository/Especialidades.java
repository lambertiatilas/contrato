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
import javax.persistence.criteria.From;
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
	
	public List<Especialidade> especialidades() {
		return this.manager.createQuery("from Especialidade", Especialidade.class).getResultList();
	}
	
	private List<Predicate> criarPredicatesParaFiltro(EspecialidadeFilter filtro, Root<Especialidade> especialidadeRoot) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (StringUtils.isNotBlank(filtro.getDescricao())) {
			predicates.add(builder.like(builder.upper(especialidadeRoot.get("descricao")), "%" + filtro.getDescricao().toUpperCase() + "%"));
		}
		
		return predicates;
	}
	
	public List<Especialidade> filtradas(EspecialidadeFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Especialidade> criteriaQuery = builder.createQuery(Especialidade.class);
		Root<Especialidade> especialidadeRoot = criteriaQuery.from(Especialidade.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, especialidadeRoot);
		
		criteriaQuery.select(especialidadeRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = especialidadeRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Especialidade> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltradas(EspecialidadeFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Especialidade> especialidadeRoot = criteriaQuery.from(Especialidade.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, especialidadeRoot);
		
		criteriaQuery.select(builder.count(especialidadeRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}