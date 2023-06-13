package br.com.alexdev.junit.builders;

import br.com.alexdev.junit.entidades.Usuario;

public class UsuarioBuilder {

	private Usuario user;
	
	private UsuarioBuilder() {}
	
	public static UsuarioBuilder umUsuario() {
		UsuarioBuilder builder = new UsuarioBuilder();
		builder.user = new Usuario();
		builder.user.setNome("usuario_1");
		return builder;
	}
	
	public Usuario agora() {
		return user;
	}
}
