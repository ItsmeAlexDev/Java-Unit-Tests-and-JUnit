package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

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
		assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		List<Filme> filme = Arrays.asList(new Filme("Filme", 10, 5.0));
		
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
	public void test25Desconto3Filme() throws Exception {
		List <Filme> filmes = asList(new Filme("Filme_1", 2, 4.0),
											new Filme("Filme_2", 2, 4.0),
											new Filme("Filme_3", 2, 4.0));

		Locacao resultado = service.alugarFilme(user, filmes);
	
		assertThat(resultado.getValor(), is(11.0));
	}
	
	@Test
	public void test50Desconto4Filme() throws Exception {
		List <Filme> filmes = asList(new Filme("Filme_1", 2, 4.0),
											new Filme("Filme_2", 2, 4.0),
											new Filme("Filme_3", 2, 4.0),
											new Filme("Filme_4", 2, 4.0));

		Locacao resultado = service.alugarFilme(user, filmes);
	
		assertThat(resultado.getValor(), is(13.0));
	}
	
	@Test
	public void test75Desconto5Filme() throws Exception {
		List <Filme> filmes = asList(new Filme("Filme_1", 2, 4.0),
									 new Filme("Filme_2", 2, 4.0),
									 new Filme("Filme_3", 2, 4.0),
									 new Filme("Filme_4", 2, 4.0),
									 new Filme("Filme_5", 2, 4.0));

		Locacao resultado = service.alugarFilme(user, filmes);
	
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void test100Desconto6Filme() throws Exception {
		List <Filme> filmes = Arrays.asList(new Filme("Filme_1", 2, 4.0),
											new Filme("Filme_2", 2, 4.0),
											new Filme("Filme_3", 2, 4.0),
											new Filme("Filme_4", 2, 4.0),
											new Filme("Filme_5", 2, 4.0),
											new Filme("Filme_6", 2, 4.0));

		Locacao resultado = service.alugarFilme(user, filmes);
	
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void retornoSegundaQuandoAlugadoNoSabado() throws Exception {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		List <Filme> filmes = Arrays.asList(new Filme("Filme_1", 2, 4.0));
		
		Locacao resultado = service.alugarFilme(user, filmes);
		
		boolean isSegunda = DataUtils.verificarDiaSemana(resultado.getDataRetorno(), Calendar.MONDAY);
		assertTrue(isSegunda);
	}
}
