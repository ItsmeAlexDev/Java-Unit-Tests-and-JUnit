package br.com.alexdev.junit.servicos;

import static br.com.alexdev.junit.builders.FilmeBuilder.umFilme;
import static br.com.alexdev.junit.builders.LocacaoBuilder.umLocacao;
import static br.com.alexdev.junit.builders.UsuarioBuilder.umUsuario;
import static br.com.alexdev.junit.matchers.MatchersLocais.caiNumaSegunda;
import static br.com.alexdev.junit.matchers.MatchersLocais.isHoje;
import static br.com.alexdev.junit.matchers.MatchersLocais.isHojeComMaisXDias;
import static br.com.alexdev.junit.utils.DataUtils.obterDataComDiferencaDias;
import static br.com.alexdev.junit.utils.DataUtils.verificarDiaSemana;
import static java.util.Arrays.asList;
import static java.util.Calendar.SATURDAY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.com.alexdev.junit.daos.LocacaoDAO;
import br.com.alexdev.junit.entidades.Filme;
import br.com.alexdev.junit.entidades.Locacao;
import br.com.alexdev.junit.entidades.Usuario;

public class LocacaoServiceTest {

	private LocacaoService service;
	private Usuario user;
	
	private LocacaoDAO dao;
	private SPCService spc;
	private EmailService email;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		service = new LocacaoService();
		user = umUsuario().agora();
		
		dao = mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);
		
		spc = mock(SPCService.class);
		service.setSPCService(spc);
		
		email = mock(EmailService.class);
		service.setEmailServices(email);
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testLocacao() throws Exception{
		assumeFalse(verificarDiaSemana(new Date(), SATURDAY));
		
		List<Filme> filme = asList(umFilme().comValor(5.0).agora());
		
		Locacao locacao = service.alugarFilme(user, filme);
		
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getDataLocacao(), isHoje());
		error.checkThat(locacao.getDataRetorno(), isHojeComMaisXDias(1));
	}
	
	@Test(expected = Exception.class)
	public void testLocacaoSemEstoque_1() throws Exception {
		List<Filme> filme = asList(umFilme().semEstoque().agora());
		
		service.alugarFilme(user, filme);
	}
	
	@Test
	public void testLocacao_usuarioVazio() {
		List<Filme> filme = asList(umFilme().agora());
		
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
		
		List <Filme> filmes = asList(umFilme().agora());
		
		Locacao resultado = service.alugarFilme(user, filmes);
		
		assertThat(resultado.getDataRetorno(), caiNumaSegunda());
	}
	
	@Test
	public void naoAlugaParaNegativadoNoSPC() {
		List <Filme> filme = asList(umFilme().agora());
		
		when(spc.isNegativado(user)).thenReturn(true);

		try {
			service.alugarFilme(user, filme);
			fail();
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Usuario Negativado"));
		}
		
		verify(spc).isNegativado(user);
	}
	
	@Test
	public void emailParaNotificarAtrasos() {
		List<Locacao> locacoes = asList(umLocacao()
				.comUsuario(user)
				.comDataRetorno(obterDataComDiferencaDias(-2))
				.agora());
		
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		service.notificarAtrasos();
		
		verify(email).notificarAtraso(user);
	}
}
