package edu.desafios.sefaz.modelo;

import java.util.Iterator;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import edu.desafios.sefaz.modelo.enums.Tipo;

@Entity
public class Telefone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int ddd;
	
	private String numero;
	
	@Enumerated(EnumType.STRING)
	private Tipo tipo;
	
	@ManyToOne
	private Usuario usuario;
	
	
	public Telefone() {
	}


	public Telefone(int ddd, String numero, Tipo tipo) {
		this.ddd = ddd;
		this.numero = numero;
		this.tipo = tipo;
	}


	public int getDdd() {
		return ddd;
	}


	public void setDdd(int ddd) {
		this.ddd = ddd;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	


	public Tipo getTipo() {
		return tipo;
	}


	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}


	public Usuario getUsuario() {
		
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@Override
	public String toString() {
		return "(" + this.ddd + ") " + this.numero + " - " + this.tipo;
	}


	
}
