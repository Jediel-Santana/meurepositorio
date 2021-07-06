package edu.desafios.sefaz.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.desafios.sefaz.modelo.Telefone;
import edu.desafios.sefaz.modelo.Usuario;
import edu.desafios.sefaz.modelo.enums.Tipo;
import edu.desafios.sefaz.service.UsuarioService;

@Named
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private Usuario usuario;

	
	@Inject
	private FacesContext context;
	
	private Long IdDoUsuarioParaAtualizar;
	
	
	private List<Usuario> usuarios;
	
	private Usuario usuarioLogado = new Usuario();
	private String telefone;
	private String tipo;
	private String confirmarSenha;

	public String logar() {

		context.getExternalContext().getFlash().setKeepMessages(true);

		Usuario usuarioBuscado = usuarioService.verificaLogin(usuario);

		if (usuarioBuscado != null) {

			context.getExternalContext().getSessionMap().put("usuarioLogado", usuarioBuscado);
			usuario = new Usuario();
			this.usuarioLogado = usuarioBuscado;
			return "home?faces-redirect=true";

		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário não encontrado!",
					"Insira um usuário válido ou cadastre-se"));
			return "";
		}

	}
	
	public String deslogar() {
		context.getExternalContext().getSessionMap().remove("usuarioLogado");
		this.usuarioLogado = null;
		return "login?faces-redirect=true";
	}

	public String gravar() {

		boolean autorizado = autorizado();

		if (autorizado) {
			if (usuario.getId() == null) {
				usuarioService.adicionar(usuario);
				usuario = new Usuario();
				setNullCampos();
				this.usuarios = usuarioService.listar();
			} else {
				usuarioService.atualizar(usuario);
				
				this.usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
				
				if(usuario.getId() == usuarioLogado.getId()) {
					usuarioLogado = usuario;
					context.getExternalContext().getSessionMap().replace("usuarioLogado", usuario);
					
				}
				
				
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário atualizado com sucesso!", "Alterações realizadas!"));
			}
		}

		return "";
	}
	
	public void remover(Usuario usuario) {
		
		if(usuario.getId() != null) {
			usuarioService.remover(usuario);
			this.usuarios = usuarioService.listar();
		}
		
		this.usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
		
		if(usuario.getId() == this.usuarioLogado.getId()) {
			context.getExternalContext().getSessionMap().remove("usuarioLogado");
			context.getApplication().getNavigationHandler().handleNavigation(context, null, "login?faces-redirect=true");
			context.renderResponse();
		}
		
	}
	
	public String atualizarUsuario(Usuario usuario) {
		
		System.out.println(usuario);
		
		this.usuario = usuario;
		
		return "atualizarUsuario?usuarioId=" + usuario.getId()  + "&faces-redirect=true";
		
		
	}
	
	
	public void carregaUsuarioPelaId() {
		
		this.usuario = usuarioService.buscarPelaId(IdDoUsuarioParaAtualizar);
		
		this.confirmarSenha = usuario.getSenha();
		
		
	}

	private boolean autorizado() {
		boolean senhaIsValida = senhaIsValida();
		boolean listaTelefoneIsEmpty = usuario.getTelefones().isEmpty();

		System.out.println(usuario.getSenha());
		System.out.println(confirmarSenha);

		if (!senhaIsValida) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"As senhas não coincidem", "Verifique campo Senha e Confirmar Senha"));
		}

		if (listaTelefoneIsEmpty) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Deve conter no minimo um telefone", "Obrigátorio um número para contato"));
		}

		if (!senhaIsValida || listaTelefoneIsEmpty) {
			return false;
		}
		
		
		Usuario buscadoPeloEmail = usuarioService.buscaUsuarioPeloEmail(usuario.getEmail());
		
		if(buscadoPeloEmail != null && buscadoPeloEmail.getId() != usuario.getId()){
			System.out.println("Não autorizado!");
			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Email já cadastrado!", "Esse email já está sendo utilizado!"));
			
			return false;
		} 
		
		
		return true;

	}

	public boolean senhaIsValida() {

		if (usuario.getSenha().equals(confirmarSenha)) {
			return true;
		}

		return false;
	}

	public void adicionarTelefone() {

		if (telefone.isEmpty()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Insira um número para adicionar", "Necessário conter um número para adicionar"));
			return;
		}
		
		Telefone telefoneOBJ = retornaTelefone(telefone);
		Tipo tipoEnum = Tipo.valueOf(tipo);
		telefoneOBJ.setTipo(tipoEnum);
		
		if(telefoneJaEstaCadastrado(telefoneOBJ)) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Telefone já cadastrado!", "Insira outro contato."));
			return;
		}
		
		
		usuario.adicionaTelefone(telefoneOBJ);
		tipo = null;
		telefone = null;

	}

	private boolean telefoneJaEstaCadastrado(Telefone telefone) {

		List<Telefone> telefonesDoUsuario = this.usuario.getTelefones();
		
		for (Telefone telefoneDoUsuario : telefonesDoUsuario) {
			
			if(telefoneDoUsuario.getNumero().equals(telefone.getNumero())) {
				return true;
			}
		}
		
		return false;
		
	}

	public void removeTelefone(Telefone telefone) {
		usuario.removeTelefone(telefone);
		this.usuarios = usuarioService.listar();
	}

	public List<Usuario> getUsuarios() {

		if (usuarios == null) {
			this.usuarios = usuarioService.listar();
		}

		return this.usuarios;
	}

	public Telefone retornaTelefone(String telefoneFormatado) {

		telefoneFormatado = telefoneFormatado.replace("(", "");
		telefoneFormatado = telefoneFormatado.replace(")", ";");
		telefoneFormatado = telefoneFormatado.replace("-", "");
		telefoneFormatado = telefoneFormatado.replace(" ", "");

		telefoneFormatado = telefoneFormatado.trim();

		String[] telefoneSplit = telefoneFormatado.split(";");

		Telefone telefone = new Telefone();

		Integer ddd = Integer.valueOf(telefoneSplit[0]);
		telefone.setDdd(ddd);
		telefone.setNumero(telefoneSplit[1]);

		return telefone;
	}

	private void setNullCampos() {
		telefone = null;
		confirmarSenha = null;
		tipo = null;
	}

	public boolean autorizadoLogin() {
		if (usuario.getEmail() == null || usuario.getSenha() == null) {
			return false;
		}

		return true;
	}

	public void validaLogin(FacesContext fc) {

		if (usuario.getEmail() == null) {
			fc.addMessage("email", new FacesMessage("Email requerido"));
			;
		}

		if (usuario.getSenha() == null) {
			fc.addMessage("senha", new FacesMessage("Senha requerida"));
			;
		}

	}

	public Tipo[] tiposTelefone() {
		Tipo[] values = Tipo.values();
		return values;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<Telefone> getTelefones() {
		return usuario.getTelefones();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	public Long getIdDoUsuarioParaAtualizar() {
		return IdDoUsuarioParaAtualizar;
	}

	public void setIdDoUsuarioParaAtualizar(Long idDoUsuarioParaAtualizar) {
		IdDoUsuarioParaAtualizar = idDoUsuarioParaAtualizar;
	}
	
	
	

}
