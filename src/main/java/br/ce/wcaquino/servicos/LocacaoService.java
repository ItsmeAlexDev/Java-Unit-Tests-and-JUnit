package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {
		
		if((usuario == null) || (filmes == null || filmes.isEmpty())) 
			throw new Exception("Usuario ou filme invalido!");
		
		for (Filme filme : filmes) 
			if(filme.getEstoque() == 0) 
				throw new Exception("Filme fora de estoque!");
		
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotal = 0d;
		for (int filmeIndex = 0; filmeIndex < filmes.size(); filmeIndex++) {
			Double valorFilme = filmes.get(filmeIndex).getPrecoLocacao();
			if (filmeIndex > 1 && filmeIndex < 6) valorFilme *= 1 - 0.25 * (filmeIndex - 1);
			valorTotal += valorFilme;
		}
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}
}