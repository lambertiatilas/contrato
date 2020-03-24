package pmvv.semsa.rh.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import pmvv.semsa.rh.model.Lotacao;
import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.model.StatusLotacao;
import pmvv.semsa.rh.repository.Lotacoes;
import pmvv.semsa.rh.repository.Profissionais;
import pmvv.semsa.rh.util.jpa.Transactional;

public class EdicaoLotacaoService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroSolicitacaoService cadastroSolicitacaoService;
	@Inject
	private Lotacoes lotacoes;
	@Inject
	private Profissionais profissionais;

	@Transactional
	public Lotacao aceitarLotacao(Lotacao lotacao) throws NegocioException {
		lotacao = lotacoes.porId(lotacao.getId());
		
		if (lotacao.isNaoAceitavelOuNaoRejeitavel()) {
			throw new NegocioException("Lotação não pode ser aceita!");
		}
		
		lotacao.setDataInicio(new Date());
		lotacao.setStatus(StatusLotacao.ATIVO);
		Profissional profissional = lotacao.getVinculo().getProfissional();
		profissional.setLocalAcesso(lotacao.getEstabelecimento());
		profissionais.guardar(profissional);
		cadastroSolicitacaoService.salvar(lotacao.getSolicitacao());
		return lotacao;
	}
	
	@Transactional
	public Lotacao rejeitarLotacao(Lotacao lotacao) throws NegocioException {
		lotacao = lotacoes.porId(lotacao.getId());
		
		if (lotacao.isNaoAceitavelOuNaoRejeitavel()) {
			throw new NegocioException("Lotação não pode ser rejeitada!");
		}
		
		lotacao.setStatus(StatusLotacao.INATIVO);
		cadastroSolicitacaoService.salvar(lotacao.getSolicitacao());
		return lotacao;
	}
}