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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import pmvv.semsa.rh.contrato.model.Profissional;
import pmvv.semsa.rh.contrato.model.Solicitacao;
import pmvv.semsa.rh.contrato.model.StatusSolicitacao;
import pmvv.semsa.rh.contrato.repository.filter.SolicitacaoFilter;
import pmvv.semsa.rh.contrato.service.NegocioException;
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
	
	public Solicitacao existe(Long estabelecimentoSolcitante) {
		try {
			return manager.createQuery("from Solicitacao where"
					+ " and estabelecimentoSolcitante = :estabelecimentoSolcitante"
					+ " status <> :status", Solicitacao.class)
					.setParameter("estabelecimentoSolcitante", estabelecimentoSolcitante)
					.setParameter("status", StatusSolicitacao.ENCERRADA.toString())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Solicitacao> filtradas(SolicitacaoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Solicitacao> criteriaQuery = builder.createQuery(Solicitacao.class);
		List<Predicate> predicates = new ArrayList<>();
		
		Root<Solicitacao> solicitacaoRoot = criteriaQuery.from(Solicitacao.class);
		Join<Solicitacao, Profissional> profissionalSolicitanteJoin = solicitacaoRoot.join("profissionalSolicitante", JoinType.INNER);
		Join<Solicitacao, Profissional> profissionalAtendenteJoin = solicitacaoRoot.join("profissionalAtendente", JoinType.LEFT);
		
		if (filtro.getDataHoraAberturaDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(solicitacaoRoot.get("dataHoraAbertura"), filtro.getDataHoraAberturaDe()));
		}
		
		if (filtro.getDataHoraAberturaAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(solicitacaoRoot.get("dataHoraAbertura"), filtro.getDataHoraAberturaAte()));
		}
		
		if (filtro.getDataHoraEncerramentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(solicitacaoRoot.get("dataHoraEncerramento"), filtro.getDataHoraEncerramentoDe()));
		}
		
		if (filtro.getDataHoraEncerramentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(solicitacaoRoot.get("dataHoraEncerramento"), filtro.getDataHoraEncerramentoAte()));
		}
		
		if (StringUtils.isNotBlank(filtro.getProfissionalSolicitante())) {
			predicates.add(builder.like(builder.upper(profissionalSolicitanteJoin.get("profissionalSolicitante")), "%" + filtro.getProfissionalSolicitante().toUpperCase() + "%"));
		}
		
		if (filtro.getEstabelecimentoSolicitante() != null) {
			predicates.add(solicitacaoRoot.get("estabelecimentoSolcitante").in(filtro.getEstabelecimentoSolicitante()));
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
		
		criteriaQuery.select(solicitacaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(solicitacaoRoot.get("id")));
		
		TypedQuery<Solicitacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
}