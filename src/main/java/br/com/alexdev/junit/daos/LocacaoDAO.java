package br.com.alexdev.junit.daos;

import java.util.List;

import br.com.alexdev.junit.entidades.Locacao;

public interface LocacaoDAO {

	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendentes();
}
