package br.com.alexdev.junit.servicos;

import static br.com.alexdev.junit.utils.DataUtils.adicionarDias;
import static br.com.alexdev.junit.utils.DataUtils.obterDataComDiferencaDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.alexdev.junit.daos.LocacaoDAO;
import br.com.alexdev.junit.entidades.Filme;
import br.com.alexdev.junit.entidades.Locacao;
import br.com.alexdev.junit.entidades.Usuario;
import br.com.alexdev.junit.utils.DataUtils;

public class LocacaoService {
	
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {
		
		if ((usuario == null) || (filmes == null || filmes.isEmpty())) 
			throw new Exception("Usuario ou filme invalido!");
		
		for (Filme filme : filmes) 
			if(filme.getEstoque() == 0) 
				throw new Exception("Filme fora de estoque!");
		
		boolean negativado;
		
		try {
			negativado = spcService.isNegativado(usuario);
		} catch (Exception e) { 
			throw new Exception("Falha no SPC");
		}
		
		if (negativado)
			throw new Exception("Usuario Negativado");
		
		Locacao locacao = new Locacao();
		
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(getNewDate());
		
		Double valorTotal = 0d;
		
		for (int filmeIndex = 0; filmeIndex < filmes.size(); filmeIndex++) {
			Double valorFilme = filmes.get(filmeIndex).getPrecoLocacao();
			if (filmeIndex > 1 && filmeIndex < 6) valorFilme *= 1 - 0.25 * (filmeIndex - 1);
			valorTotal += valorFilme;
		
		}
		locacao.setValor(valorTotal);

		Date dataEntrega = getNewDate();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY))
			dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		dao.salvar(locacao);
		
		return locacao;
	}
	
	public Date getNewDate() {
		return new Date();
	}
	
	public void notificarAtrasos() {
		List<Locacao> locacoes = dao.obterLocacoesPendentes();
		for (Locacao locacao : locacoes)
			if (locacao.getDataRetorno().before(getNewDate()))
				emailService.notificarAtraso(locacao.getUsuario());
	}
	
	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao newLocacao = new Locacao();
		
		newLocacao.setUsuario(locacao.getUsuario());
		newLocacao.setFilmes(locacao.getFilmes());
		newLocacao.setValor(locacao.getValor() * dias);
		
		newLocacao.setDataLocacao(getNewDate());
		newLocacao.setDataRetorno(obterDataComDiferencaDias(dias));
		
		dao.salvar(newLocacao);
	}
}