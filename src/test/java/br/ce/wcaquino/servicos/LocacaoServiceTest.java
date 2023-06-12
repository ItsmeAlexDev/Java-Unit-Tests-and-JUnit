package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.verificarDiaSemana;
import static java.util.Arrays.asList;
import static java.util.Calendar.SATURDAY;
import static matchers.MatchersLocais.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import matchers.MatchersLocais;

public class LocacaoServiceTest {

	private LocacaoService service;
	private Usuario user;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		service = new LocacaoService();
		user = new Usuario("User_1");
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testLocacao() throws Exception{
		assumeFalse(verificarDiaSemana(new Date(), SATURDAY));
		
		List<Filme> filme = asList(new Filme("Filme", 10, 5.0));
		
		Locacao locacao = service.alugarFilme(user, filme);
		
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
	}
	
	@Test(expected = Exception.class)
	public void testLocacaoSemEstoque_1() throws Exception {
		List<Filme> filme = asList(new Filme("Filme", 0, 5.0));
		
		service.alugarFilme(user, filme);
	}
	
	@Test
	public void testLocacaoSemEstoque_2() {
		List<Filme> filme = asList(new Filme("Filme", 0, 5.0));
		
		
		try {
			service.alugarFilme(user, filme);
			fail();
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme fora de estoque!"));
		}
	}
	
	@Test
	public void testLocacaoSemEstoque_3() throws Exception {
		List<Filme> filme = asList(new Filme("Filme", 0, 5.0));
		
		exception.expect(Exception.class);
		
		service.alugarFilme(user, filme);
	}
	
	@Test
	public void testLocacao_usuarioVazio() {
		List<Filme> filme = asList(new Filme("Filme", 10, 5.0));
		
		try {
			service.alugarFilme(null, filme);
			fail();
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Usuario ou filme invalido!"));
		}
	}
	
	@Test
	public void testLocacao_filmeVazio() throws Exception {
		exception.expect(Exception.class);
		exception.expectMessage("Usuario ou filme invalido!");
		
		service.alugarFilme(user, null);
	}

	
	@Test
	public void retornoSegundaQuandoAlugadoNoSabado() throws Exception {
		Assume.assumeTrue(verificarDiaSemana(new Date(), SATURDAY));
		
		List <Filme> filmes = asList(new Filme("Filme_1", 2, 4.0));
		
		Locacao resultado = service.alugarFilme(user, filmes);
		
		assertThat(resultado.getDataRetorno(), caiNumaSegunda());
	}
}
