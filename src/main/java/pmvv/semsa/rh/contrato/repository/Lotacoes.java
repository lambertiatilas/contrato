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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.model.StatusLotacao;
import pmvv.semsa.rh.contrato.model.Vinculo;
import pmvv.semsa.rh.contrato.repository.filter.LotacaoFilter;
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
	
	public Lotacao existe(Lotacao lotacao) {
		try {
			return manager.createQuery("from Lotacao"
					+ " where estabelecimento = :estabelecimento"
					+ " and (status = :ativo or status = :pendente)"
				, Lotacao.class)
				.setParameter("estabelecimento", lotacao.getEstabelecimento())
				.setParameter("ativo", StatusLotacao.ATIVO)
				.setParameter("pendente", StatusLotacao.PENDENTE)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	private List<Predicate> criarPredicatesParaFiltro(LotacaoFilter filtro, Root<Lotacao> lotacaoRoot, From<?, ?> vinculoJoin, From<?, ?> profissionalJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (StringUtils.isNotBlank(filtro.getCpf())) {
			predicates.add(builder.equal(profissionalJoin.get("cpf"), filtro.getCpf()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(profissionalJoin.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		if (filtro.getEspecialidade() != null) {
			predicates.add(vinculoJoin.get("especialidade").in(filtro.getEspecialidade()));
		}
		
		if (filtro.getTipoVinculo() != null) {
			predicates.add(vinculoJoin.get("tipo").in(filtro.getTipoVinculo()));
		}
		
		if (filtro.getCargaHoraria() != null) {
			predicates.add(vinculoJoin.get("cargaHoraria").in(filtro.getCargaHoraria()));
		}
		
		if (filtro.getEstabelecimento() != null) {
			predicates.add(lotacaoRoot.get("estabelecimento").in(filtro.getEstabelecimento()));
		}
		
		predicates.add(builder.isNotNull(lotacaoRoot.get("dataInicio")));
		
		return predicates;
	}
	
	public List<Lotacao> filtradas(LotacaoFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lotacao> criteriaQuery = builder.createQuery(Lotacao.class);
		Root<Lotacao> lotacaoRoot = criteriaQuery.from(Lotacao.class);
		From<?, ?> vinculoJoin = (From<?, ?>) lotacaoRoot.fetch("vinculo", JoinType.INNER);
		From<?, ?> profissionalJoin = (From<?, ?>) vinculoJoin.fetch("profissional", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, lotacaoRoot, vinculoJoin, profissionalJoin);
		
		criteriaQuery.select(lotacaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = lotacaoRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Lotacao> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltradas(LotacaoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Lotacao> lotacaoRoot = criteriaQuery.from(Lotacao.class);
		Join<Lotacao, Vinculo> vinculoJoin = lotacaoRoot.join("vinculo", JoinType.INNER);
		Join<Vinculo, Profissional> profissionalJoin = vinculoJoin.join("profissional", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, lotacaoRoot, vinculoJoin, profissionalJoin);
		
		criteriaQuery.select(builder.count(lotacaoRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}