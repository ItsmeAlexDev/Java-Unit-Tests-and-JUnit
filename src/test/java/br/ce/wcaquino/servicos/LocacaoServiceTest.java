package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testLocacao() throws Exception{
		LocacaoService service = new LocacaoService();
		Usuario user = new Usuario("User_1");
		Filme filme = new Filme("Filme", 10, 5.0);
		
		Locacao locacao = service.alugarFilme(user, filme);
		
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		
	
	}
	
	@Test(expected = Exception.class)
	public void testLocacaoSemEstoque_1() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario user = new Usuario("User_1");
		Filme filme = new Filme("Filme", 0, 5.0);
		
		service.alugarFilme(user, filme);
		
	}
	
	@Test
	public void testLocacaoSemEstoque_2() {
		LocacaoService service = new LocacaoService();
		Usuario user = new Usuario("User_1");
		Filme filme = new Filme("Filme", 0, 5.0);
		
		
		try {
			service.alugarFilme(user, filme);
			//Assert.fail("Excecao esperada!");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme fora de estoque!"));
		}
		
	}
	
	@Test
	public void testLocacaoSemEstoque_3() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario user = new Usuario("User_1");
		Filme filme = new Filme("Filme", 0, 5.0);
		
		exception.expect(Exception.class);
		
		service.alugarFilme(user, filme);
		
	}
}
