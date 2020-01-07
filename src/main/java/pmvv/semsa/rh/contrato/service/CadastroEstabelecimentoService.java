package pmvv.semsa.rh.contrato.service;

import java.io.Serializable;

import javax.inject.Inject;

import pmvv.semsa.rh.contrato.model.Estabelecimento;
import pmvv.semsa.rh.contrato.repository.Estabelecimentos;
import pmvv.semsa.rh.contrato.util.jpa.Transactional;

public class CadastroEstabelecimentoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Estabelecimentos estabelecimentos;
	
	@Transactional
	public Estabelecimento salvar(Estabelecimento estabelecimento) throws NegocioException {
		Estabelecimento estabelecimentoExiste = estabelecimentos.porDescricao(estabelecimento.getDescricao());
		
		if (estabelecimentoExiste != null && !estabelecimentoExiste.equals(estabelecimento)) {
			throw new NegocioException("O estabelecimento " + estabelecimento.getDescricao() + " já esta cadastrado.");
		}
		
		return estabelecimentos.guardar(estabelecimento);
	}
}