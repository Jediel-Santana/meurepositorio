package edu.desafios.sefaz.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import edu.desafios.sefaz.modelo.Usuario;

@SuppressWarnings("serial")
@Stateless
public class UsuarioDAO implements Serializable {
	
	
	@PersistenceContext
	private EntityManager manager;
	
	
	public void adicionar(Usuario usuario) {
		manager.persist(usuario);
	}
	
	
	public void remover(Usuario usuario) {
		usuario = manager.merge(usuario);
		manager.remove(usuario);
	}
	
	
	public void atualizar(Usuario usuario) {
		manager.merge(usuario);
	}
	
	public Usuario verificaLogin(Usuario usuario) {
		
		String jpql = "SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha";
		
		
		
		try {
			Usuario usuarioBuscado = manager.createQuery(jpql, Usuario.class)
					.setParameter("email", usuario.getEmail())
					.setParameter("senha", usuario.getSenha())
					.getSingleResult();
		
			return usuarioBuscado;
			
		} catch (NoResultException e) {
			return null;
		}
		
		
	}


	public List<Usuario> listar() {
		return manager.createQuery("SELECT u from Usuario u", Usuario.class).getResultList();
	}


	public Usuario buscarPelaId(Long id) {
		return manager.find(Usuario.class, id);
	}


	public Usuario buscaUsuarioPeloEmail(String email) {
		String jpql = "SELECT u FROM Usuario u WHERE u.email = :email";
		
		Usuario usuario = null;
		
		try {
			usuario = manager.createQuery(jpql, Usuario.class)
				.setParameter("email", email).getSingleResult();
			return usuario;
		
		} catch(NoResultException ex) {
			return null;
		}
		
	}
	
}
