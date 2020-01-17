package pmvv.semsa.rh.contrato.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pmvv.semsa.rh.contrato.model.Lotacao;
import pmvv.semsa.rh.contrato.security.Seguranca;

@Named
@RequestScoped
public class LotacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Seguranca seguranca;
	private Lotacao lotacao;
	private List<Lotacao> listaLotacoes = new ArrayList<>();
	
	public Lotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(Lotacao lotacao) {
		this.lotacao = lotacao;
		System.out.println(lotacao.getId());
	}

	public List<Lotacao> getListaLotacoes() {
		return listaLotacoes;
	}
	
	public void inicializar() {
		if (lotacao == null) {
			lotacao = new Lotacao();
		}
		
		listaLotacoes = seguranca.getUsuario().listaLotacoes();
	}
}