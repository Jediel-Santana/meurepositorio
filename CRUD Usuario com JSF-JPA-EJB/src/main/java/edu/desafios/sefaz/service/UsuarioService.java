
package edu.desafios.sefaz.service;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.desafios.sefaz.dao.UsuarioDAO;
import edu.desafios.sefaz.modelo.Usuario;

@SuppressWarnings("serial")
@Stateless
public class UsuarioService implements Serializable {
	
	@Inject 
	private UsuarioDAO usuarioDAO;

	public void adicionar(Usuario usuario) {
		usuarioDAO.adicionar(usuario);
	}

	public void remover(Usuario usuario) {
		usuarioDAO.remover(usuario);
	}

	public void atualizar(Usuario usuario) {
		usuarioDAO.atualizar(usuario);
	}

	public Usuario verificaLogin(Usuario usuario) {
		return usuarioDAO.verificaLogin(usuario);
	}

	public List<Usuario> listar() {
		
		return usuarioDAO.listar();
	}

	public Usuario buscarPelaId(Long id) {
		
		return usuarioDAO.buscarPelaId(id);
	}

	public Usuario buscaUsuarioPeloEmail(String email) {
		return usuarioDAO.buscaUsuarioPeloEmail(email);
	}

	
	
	
}
