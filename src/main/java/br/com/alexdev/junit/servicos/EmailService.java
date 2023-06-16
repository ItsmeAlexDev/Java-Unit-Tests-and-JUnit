package br.com.alexdev.junit.servicos;

import br.com.alexdev.junit.entidades.Usuario;

public interface EmailService {

	public void notificarAtraso(Usuario user);
}
