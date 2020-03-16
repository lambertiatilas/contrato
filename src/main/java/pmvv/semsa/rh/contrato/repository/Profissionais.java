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

import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.model.Status;
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
	
	public Profissional porCpf(String cpf) {
		try {
			return manager.createQuery("from Profissional where cpf = :cpf", Profissional.class).setParameter("cpf", cpf).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Profissional ativo(String cpf) {
		try {
			return manager.createQuery("from Profissional"
					+ " where cpf = :cpf"
					+ " and status = :status"
					+ " and localAcesso is not null"
				, Profissional.class)
				.setParameter("cpf", cpf)
				.setParameter("status", Status.ATIVO)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Profissional> profissionaisSolicitantes() {
		return manager.createQuery("select profissional from Solicitacao solicitacao inner join solicitacao.profissionalSolicitante profissional"
				+ " order by profissional.nome"
			, Profissional.class).getResultList();
	}
	
	public List<Profissional> profissionaisAtendentes() {
		return manager.createQuery("select profissional from Solicitacao solicitacao inner join solicitacao.profissionalAtendente profissional"
				+ " order by profissional.nome"
			, Profissional.class).getResultList();
	}
	
	private List<Predicate> criarPredicatesParaFiltro(ProfissionalFilter filtro, Root<Profissional> profissionalRoot) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(profissionalRoot.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getCpf())) {
			predicates.add(builder.equal(profissionalRoot.get("cpf"), filtro.getCpf()));
		}
		
		if (filtro.getStatus() != null) {
			predicates.add(profissionalRoot.get("status").in(filtro.getStatus()));
		}
		
		return predicates;
	}
	
	public List<Profissional> filtrados(ProfissionalFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Profissional> criteriaQuery = builder.createQuery(Profissional.class);
		Root<Profissional> profissionalRoot = criteriaQuery.from(Profissional.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, profissionalRoot);
		
		criteriaQuery.select(profissionalRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(profissionalRoot.get("nome")));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = profissionalRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Profissional> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(ProfissionalFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Profissional> profissionalRoot = criteriaQuery.from(Profissional.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, profissionalRoot);
		
		criteriaQuery.select(builder.count(profissionalRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(profissionalRoot.get("nome")));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}