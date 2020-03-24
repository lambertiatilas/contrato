package pmvv.semsa.rh.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import pmvv.semsa.rh.model.Lotacao;
import pmvv.semsa.rh.model.Profissional;
import pmvv.semsa.rh.model.StatusLotacao;
import pmvv.semsa.rh.model.Vinculo;
import pmvv.semsa.rh.repository.Lotacoes;
import pmvv.semsa.rh.repository.Profissionais;
import pmvv.semsa.rh.util.jpa.Transactional;

public class CadastroLotacaoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Lotacoes lotacoes;
	@Inject
	private Profissionais profissionais;
	
	@Transactional
	public Lotacao salvar(Vinculo vinculo, Lotacao lotacao) throws NegocioException {
		Lotacao lotacaoExiste = lotacoes.existe(vinculo);
		
		if (lotacaoExiste != null && !lotacaoExiste.equals(lotacao)) {
			throw new NegocioException("O profissional já possui uma lotação ativa ou pendete neste vínculo!");
		}
		
		if (lotacao.isNovo()) {
			lotacao.setStatus(StatusLotacao.ATIVO);
			lotacao.setVinculo(vinculo);
			lotacao.getVinculo().getProfissional().setLocalAcesso(lotacao.getEstabelecimento());
			profissionais.guardar(lotacao.getVinculo().getProfissional());
		}
		
		if (lotacao.isExistente() && lotacao.isAtivo()) {
			Profissional profissional = lotacao.getVinculo().getProfissional();
			profissional.setLocalAcesso(lotacao.getEstabelecimento());
			profissionais.guardar(profissional);
			
		}
		
		if (lotacao.isExistente() && lotacao.isInativo()) {
			lotacao.setDataFim(new Date());
		}
		
		return lotacoes.guardar(lotacao);
	}
}