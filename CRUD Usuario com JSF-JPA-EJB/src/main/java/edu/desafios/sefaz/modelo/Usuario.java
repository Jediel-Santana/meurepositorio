package edu.desafios.sefaz.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class Usuario implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	private String email;
	
	private String senha;
	
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "usuario", fetch = FetchType.EAGER)
	private List<Telefone> telefones = new ArrayList();
	
	public Usuario() {
	}

	public Usuario(String nome, String senha, List<Telefone> telefones) {
		super();
		this.nome = nome;
		this.senha = senha;
		this.telefones = telefones;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public Long getId() {
		return id;
	}
	
	
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void adicionaTelefone(Telefone telefone) {
		telefone.setUsuario(this);
		this.telefones.add(telefone);
	}

	public void removeTelefone(Telefone telefone) {
		this.telefones.remove(telefone);
	}

	@Override
	public String toString() {
		return "Usuario [nome=" + nome + ", email=" + email + ", senha=" + senha + ", telefones=" + telefones + "]";
	}

	
	
	
	
}
