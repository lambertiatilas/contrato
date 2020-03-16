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

import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.model.StatusSolicitacao;
import pmvv.semsa.rh.contrato.repository.filter.SolicitacaoFilter;
import pmvv.semsa.rh.contrato.service.NegocioException;
import pmvv.semsa.rh.contrato.util.date.DateUtil;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class Solicitacoes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public Solicitacao guardar(Solicitacao solicitacao) {
		return manager.merge(solicitacao);
	}
	
	@Transactional
	public void remover(Solicitacao solicitacao) throws NegocioException {
		try {
			solicitacao = porId(solicitacao.getId());
			manager.remove(solicitacao);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Solicitação não pode ser excluída.");
		}
	}
	
	public Solicitacao porId(Long id) {
		return manager.find(Solicitacao.class, id);
	}
	
	public Solicitacao porEstabelecimentoSolicitante(Estabelecimento estabelecimentoSolicitante, Long id) {
		try {
			return manager.createQuery("from Solicitacao"
				+ " where estabelecimentoSolicitante = :estabelecimentoSolicitante"
				+ " and id = :id"
			, Solicitacao.class)
			.setParameter("estabelecimentoSolicitante", estabelecimentoSolicitante)
			.setParameter("id", id)
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Solicitacao existe(Estabelecimento estabelecimentoSolicitante) {
		try {
			return manager.createQuery("from Solicitacao"
					+ " where estabelecimentoSolicitante = :estabelecimentoSolicitante"
					+ " and (status <> :finalizada and status <> :cancelada)"
				, Solicitacao.class)
				.setParameter("estabelecimentoSolicitante", estabelecimentoSolicitante)
				.setParameter("finalizada", StatusSolicitacao.FINALIZADA)
				.setParameter("cancelada", StatusSolicitacao.CANCELADA)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Solicitacao> solicitacoesEnviadas() {
		return manager.createQuery("from Solicitacao where"
				+ " status = :status"
				+ " order by dataHoraAbertura"
			, Solicitacao.class)
			.setParameter("status", StatusSolicitacao.ENVIADA)
			.getResultList();
	}
	
	public List<Solicitacao> solicitacoesAtendidas() {
		return manager.createQuery("from Solicitacao where"
				+ " status = :status"
				+ " order by dataHoraAbertura"
			, Solicitacao.class)
			.setParameter("status", StatusSolicitacao.ATENDIDA)
			.getResultList();
	}
	
	public Solicitacao solicitacaoPendete(Estabelecimento estabelecimento) {
		try {
			return manager.createQuery("from Solicitacao"
				+ " where (status = :naoEnviada or status = :enviada or status = :atendida)"
				+ " and estabelecimentoSolicitante = :estabelecimento"
			, Solicitacao.class)
			.setParameter("naoEnviada", StatusSolicitacao.NAO_ENVIADA)
			.setParameter("enviada", StatusSolicitacao.ENVIADA)
			.setParameter("atendida", StatusSolicitacao.ATENDIDA)
			.setParameter("estabelecimento", estabelecimento)
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	private List<Predicate> criarPredicatesParaFiltro(SolicitacaoFilter filtro, Root<Solicitacao> solicitacaoRoot, From<?, ?> profissionalSolicitanteJoin, From<?, ?> estabelecimentoSolicitanteJoin, From<?, ?> profissionalAtendenteJoin, From<?, ?> estabelecimentoAtendenteJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(solicitacaoRoot.get("id"), filtro.getId()));
		}
		
		if (filtro.getDataHoraAberturaDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(solicitacaoRoot.get("dataHoraAbertura"), filtro.getDataHoraAberturaDe()));
		}
		
		if (filtro.getDataHoraAberturaAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(solicitacaoRoot.get("dataHoraAbertura"), DateUtil.maisUmDia(filtro.getDataHoraAberturaAte())));
		}
		
		if (filtro.getDataHoraEncerramentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(solicitacaoRoot.get("dataHoraEncerramento"), filtro.getDataHoraEncerramentoDe()));
		}
		
		if (filtro.getDataHoraEncerramentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(solicitacaoRoot.get("dataHoraEncerramento"), DateUtil.maisUmDia(filtro.getDataHoraEncerramentoAte())));
		}
		
		if (StringUtils.isNotBlank(filtro.getProfissionalSolicitante())) {
			predicates.add(builder.like(builder.upper(profissionalSolicitanteJoin.get("profissionalSolicitante")), "%" + filtro.getProfissionalSolicitante().toUpperCase() + "%"));
		}
		
		if (filtro.getEstabelecimentoSolicitante() != null) {
			predicates.add(solicitacaoRoot.get("estabelecimentoSolicitante").in(filtro.getEstabelecimentoSolicitante()));
		}
		
		if (StringUtils.isNotBlank(filtro.getProfissionalAtendente())) {
			predicates.add(builder.like(builder.upper(profissionalAtendenteJoin.get("profissionalAtendente")), "%" + filtro.getProfissionalAtendente().toUpperCase() + "%"));
		}
		
		if (filtro.getEstabelecimentoAtendente() != null) {
			predicates.add(solicitacaoRoot.get("estabelecimentoAtendente").in(filtro.getEstabelecimentoAtendente()));
		}
		
		if (filtro.getStatus() != null) {
			predicates.add(solicitacaoRoot.get("status").in(filtro.getStatus()));
		}
		
		return predicates;
	}
	
	public List<Solicitacao> filtradas(SolicitacaoFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Solicitacao> criteriaQuery = builder.createQuery(Solicitacao.class);
		Root<Solicitacao> solicitacaoRoot = criteriaQuery.from(Solicitacao.class);
		From<?, ?> profissionalSolicitanteJoin = (From<?, ?>) solicitacaoRoot.fetch("profissionalSolicitante", JoinType.INNER);
		From<?, ?> estabelecimentoSolicitanteJoin = (From<?, ?>) solicitacaoRoot.fetch("estabelecimentoSolicitante", JoinType.INNER);
		From<?, ?> profissionalAtendenteJoin = (From<?, ?>) solicitacaoRoot.fetch("profissionalAtendente", JoinType.LEFT);
		From<?, ?> estabelecimentoAtendenteJoin = (From<?, ?>) solicitacaoRoot.fetch("estabelecimentoAtendente", JoinType.LEFT);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, solicitacaoRoot, profissionalSolicitanteJoin, estabelecimentoSolicitanteJoin, profissionalAtendenteJoin, estabelecimentoAtendenteJoin);
		
		criteriaQuery.select(solicitacaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.desc(solicitacaoRoot.get("dataHoraAbertura")));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = solicitacaoRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("profissionalSolicitante.")) {
				orderByFromEntity = profissionalSolicitanteJoin;
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("estabelecimentoSolicitante.")) {
				orderByFromEntity = estabelecimentoSolicitanteJoin;
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("profissionalAtendente.")) {
				orderByFromEntity = profissionalAtendenteJoin;
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("estabelecimentoAtendente.")) {
				orderByFromEntity = estabelecimentoAtendenteJoin;
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Solicitacao> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltradas(SolicitacaoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Solicitacao> solicitacaoRoot = criteriaQuery.from(Solicitacao.class);
		Join<Solicitacao, Profissional> profissionalSolicitanteJoin = solicitacaoRoot.join("profissionalSolicitante", JoinType.INNER);
		Join<Solicitacao, Estabelecimento> estabelecimentoSolicitanteJoin = solicitacaoRoot.join("estabelecimentoSolicitante", JoinType.INNER);
		Join<Solicitacao, Profissional> profissionalAtendenteJoin = solicitacaoRoot.join("profissionalAtendente", JoinType.LEFT);
		Join<Solicitacao, Estabelecimento> estabelecimentoAtendenteJoin = solicitacaoRoot.join("estabelecimentoAtendente", JoinType.LEFT);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, solicitacaoRoot, profissionalSolicitanteJoin, estabelecimentoSolicitanteJoin, profissionalAtendenteJoin, estabelecimentoAtendenteJoin);
		
		criteriaQuery.select(builder.count(solicitacaoRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.desc(solicitacaoRoot.get("dataHoraAbertura")));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}