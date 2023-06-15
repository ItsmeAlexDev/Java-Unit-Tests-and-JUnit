package br.com.alexdev.junit.servicos;

import static br.com.alexdev.junit.builders.FilmeBuilder.umFilme;
import static br.com.alexdev.junit.builders.UsuarioBuilder.umUsuario;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.alexdev.junit.daos.LocacaoDAO;
import br.com.alexdev.junit.entidades.Filme;
import br.com.alexdev.junit.entidades.Locacao;
import br.com.alexdev.junit.entidades.Usuario;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	
	private LocacaoService service;
	private Usuario user;
	
	@Parameter
	public List<Filme> filmes;
	@Parameter(value=1)
	public Double valorLocacao;
	@Parameter(value=2)
	public String cenario;
	
	@Before
	public void setup() {
		service = new LocacaoService();
		user = umUsuario().agora();
		LocacaoDAO dao = mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);
	}
	
	private static Filme filme1 = umFilme().agora();
	private static Filme filme2 = umFilme().agora();
	private static Filme filme3 = umFilme().agora();
	private static Filme filme4 = umFilme().agora();
	private static Filme filme5 = umFilme().agora();
	private static Filme filme6 = umFilme().agora();
	private static Filme filme7 = umFilme().agora();
	
	@Parameters(name="{2}")
	public static Collection<Object[]> getParametros() {
		return asList(new Object[][] {
			{asList(filme1, filme2), 8.0, "2 filmes: 0%"},
			{asList(filme1, filme2, filme3), 11.0, "3 filmes: 25%"},
			{asList(filme1, filme2, filme3, filme4), 13.0, "4 filmes: 50%"},
			{asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 filmes: 75%"},
			{asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 filmes: 100%"},
			{asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 filmes: 0%"}
		});
	}
	
	@Test
	public void calculoLocacaoValorComDescontos() throws Exception {
		Locacao resultado = service.alugarFilme(user, filmes);
	
		assertThat(resultado.getValor(), is(valorLocacao));
	}
}
