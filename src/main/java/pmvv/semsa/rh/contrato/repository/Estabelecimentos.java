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

import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.repository.filter.EstabelecimentoFilter;
import pmvv.semsa.rh.contrato.repository.filter.OrdenacaoFilter;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class Estabelecimentos extends OrdenacaoFilter implements Serializable {

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
	
	public List<Estabelecimento> estabelecimentos() {
		return this.manager.createQuery("from Estabelecimento", Estabelecimento.class).getResultList();
	}
	
	private List<Predicate> criarPredicatesParaFiltro(EstabelecimentoFilter filtro, Root<Estabelecimento> estabelecimentoRoot) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (StringUtils.isNotBlank(filtro.getDescricao())) {
			predicates.add(builder.like(builder.upper(estabelecimentoRoot.get("descricao")), "%" + filtro.getDescricao().toUpperCase() + "%"));
		}
		
		return predicates;
	}
	
	public List<Estabelecimento> filtrados(EstabelecimentoFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Estabelecimento> criteriaQuery = builder.createQuery(Estabelecimento.class);
		Root<Estabelecimento> estabelecimentoRoot = criteriaQuery.from(Estabelecimento.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, estabelecimentoRoot);
		
		criteriaQuery.select(estabelecimentoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = estabelecimentoRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Estabelecimento> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(EstabelecimentoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Estabelecimento> estabelecimentoRoot = criteriaQuery.from(Estabelecimento.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, estabelecimentoRoot);
		
		criteriaQuery.select(builder.count(estabelecimentoRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}