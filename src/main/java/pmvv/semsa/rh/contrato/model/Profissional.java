package pmvv.semsa.rh.contrato.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import pmvv.semsa.rh.contrato.util.string.Caracteres;

@Entity
@Table(name = "profissional")
public class Profissional implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String cpf;
	private String telefone;
	private String senha;
	private List<Vinculo> vinculos = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
	@Size(max = 80)
	@Column(length = 80, nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = Caracteres.trimAll(nome);
	}

	@NotBlank
	@CPF(message = "inválido!")
	@Column(columnDefinition = "CHAR(14)", nullable = false, unique = true)
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@NotBlank
	@Size(max = 15)
	@Column(length = 15)
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Size(max = 60)
	@Column(columnDefinition = "CHAR(60)", nullable = false)
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<Vinculo> getVinculos() {
		return vinculos;
	}

	public void setVinculos(List<Vinculo> vinculos) {
		this.vinculos = vinculos;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profissional other = (Profissional) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Transient
	public boolean isNovo() {
		return id == null;
	}
	
	public String gerarSenha() {
		return Caracteres.encoder(cpf.replaceAll("[^0-9]", "") + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
	}
	
	public List<Lotacao> listaLotacoes() {
		List<Lotacao> listaLotacoes = new ArrayList<>();
		
		for (Vinculo vinculo : vinculos) {
			listaLotacoes.addAll(vinculo.getLotacoes());
		}
		
		return listaLotacoes;
	}
	
	public List<Grupo> listaGrupos() {
		List<Grupo> listaGrupos = new ArrayList<>();
		
		for (Vinculo vinculo : vinculos) {
			for (Lotacao lotacao : vinculo.getLotacoes()) {
				listaGrupos.addAll(lotacao.getGrupos());
			}
		}
		
		return listaGrupos;
	}
}