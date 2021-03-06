package pmvv.semsa.rh.repository;

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

import pmvv.semsa.rh.model.Especialidade;
import pmvv.semsa.rh.model.Estabelecimento;
import pmvv.semsa.rh.model.Lotacao;
import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.model.Status;
import pmvv.semsa.rh.model.StatusLotacao;
import pmvv.semsa.rh.model.Vinculo;
import pmvv.semsa.rh.repository.filter.LotacaoFilter;
import pmvv.semsa.rh.service.NegocioException;
import pmvv.semsa.rh.util.date.DateUtil;
import pmvv.semsa.rh.util.jpa.Transactional;

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
	
	public Lotacao existe(Vinculo vinculo) {
		try {
			return manager.createQuery("from Lotacao"
				+ " where (status = :ativo or status = :pendente)"
				+ " and vinculo = :vinculo"
			, Lotacao.class)
			.setParameter("ativo", StatusLotacao.ATIVO)
			.setParameter("pendente", StatusLotacao.PENDENTE)
			.setParameter("vinculo", vinculo)
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Lotacao> vinculosProximoFim() {
		return manager.createQuery("select lotacao from Lotacao lotacao inner join lotacao.vinculo vinculo inner join vinculo.profissional profissional"
				+ " where vinculo.status = :statusVinculo"
				+ " and lotacao.status = :statusLotacao"
				+ " and vinculo.dataFim <= :data"
				+ " order by vinculo.dataFim, profissional.nome"
			, Lotacao.class)
			.setParameter("statusVinculo", Status.ATIVO)
			.setParameter("statusLotacao", StatusLotacao.ATIVO)
			.setParameter("data", DateUtil.maisDias(30))
			.getResultList();
	}
	
	public List<Lotacao> vinculosProximoFimPorEstabelecimento(Estabelecimento estabelecimento) {
		return manager.createQuery("select lotacao from Lotacao lotacao inner join lotacao.vinculo vinculo inner join vinculo.profissional profissional"
				+ " where vinculo.status = :statusVinculo"
				+ " and lotacao.status = :statusLotacao"
				+ " and vinculo.dataFim <= :data"
				+ " and lotacao.estabelecimento = :estabelecimento"
				+ " order by vinculo.dataFim, profissional.nome"
			, Lotacao.class)
			.setParameter("statusVinculo", Status.ATIVO)
			.setParameter("statusLotacao", StatusLotacao.ATIVO)
			.setParameter("data", DateUtil.maisDias(30))
			.setParameter("estabelecimento", estabelecimento)
			.getResultList();
	}
	
	private List<Predicate> criarPredicatesParaFiltro(LotacaoFilter filtro, Root<Lotacao> lotacaoRoot, From<?, ?> vinculoJoin,  From<?, ?> profissionalJoin, From<?, ?> estabelecimentoJoin, From<?, ?> especialidadeJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (StringUtils.isNotBlank(filtro.getCpf())) {
			predicates.add(builder.equal(profissionalJoin.get("cpf"), filtro.getCpf()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(profissionalJoin.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		if (filtro.getMatricula() != null) {
			predicates.add(builder.equal(vinculoJoin.get("matricula"), filtro.getMatricula()));
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
		
		if (filtro.getStatus() != null) {
			predicates.add(lotacaoRoot.get("status").in(filtro.getStatus()));
		}
		
		if (filtro.getDataFimDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(vinculoJoin.get("dataFim"), filtro.getDataFimDe()));
		}
		
		if (filtro.getDataFimAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(vinculoJoin.get("dataFim"), DateUtil.maisUmDia(filtro.getDataFimAte())));
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
		From<?, ?> estabelecimentoJoin = (From<?, ?>) lotacaoRoot.fetch("estabelecimento", JoinType.INNER);
		From<?, ?> especialidadeJoin = (From<?, ?>) vinculoJoin.fetch("especialidade", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, lotacaoRoot, vinculoJoin, profissionalJoin, estabelecimentoJoin, especialidadeJoin);
		
		criteriaQuery.select(lotacaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(profissionalJoin.get("nome")));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = lotacaoRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("vinculo.")) {
				orderByFromEntity = vinculoJoin;
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("profissional.")) {
				orderByFromEntity = profissionalJoin;
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("estabelecimento.")) {
				orderByFromEntity = estabelecimentoJoin;
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("especialidade.")) {
				orderByFromEntity = especialidadeJoin;
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
		Join<Lotacao, Estabelecimento> estabelecimentoJoin = lotacaoRoot.join("estabelecimento", JoinType.INNER);
		Join<Vinculo, Especialidade> especialidadeJoin = vinculoJoin.join("especialidade", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, lotacaoRoot, vinculoJoin, profissionalJoin, estabelecimentoJoin, especialidadeJoin);
		
		criteriaQuery.select(builder.count(lotacaoRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(profissionalJoin.get("nome")));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}