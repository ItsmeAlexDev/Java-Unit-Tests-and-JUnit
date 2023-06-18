package br.com.alexdev.junit.servicos;

import static br.com.alexdev.junit.builders.FilmeBuilder.umFilme;
import static br.com.alexdev.junit.builders.LocacaoBuilder.umLocacao;
import static br.com.alexdev.junit.builders.UsuarioBuilder.umUsuario;
import static br.com.alexdev.junit.matchers.MatchersLocais.caiNumaSegunda;
import static br.com.alexdev.junit.matchers.MatchersLocais.isHoje;
import static br.com.alexdev.junit.matchers.MatchersLocais.isHojeComMaisXDias;
import static br.com.alexdev.junit.utils.DataUtils.verificarDiaSemana;
import static java.util.Arrays.asList;
import static java.util.Calendar.SATURDAY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import br.com.alexdev.junit.daos.LocacaoDAO;
import br.com.alexdev.junit.entidades.Filme;
import br.com.alexdev.junit.entidades.Locacao;
import br.com.alexdev.junit.entidades.Usuario;

public class LocacaoServiceTest {

	private Usuario user;
	
	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private LocacaoDAO dao;
	@Mock
	private SPCService spc;
	@Mock
	private EmailService email;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		service = new LocacaoService();
		user = umUsuario().agora();
		
		initMocks(this);
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
	public void naoAlugaParaNegativadoNoSPC() throws Exception {
		List <Filme> filme = asList(umFilme().agora());
		
		when(spc.isNegativado(any(Usuario.class))).thenReturn(true);

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
		user = umUsuario().comNome("user atrasado").agora();
		Usuario user2 = umUsuario().comNome("user em dia").agora();
		Usuario user3 = umUsuario().comNome("outro user atrasado").agora();
		
		List<Locacao> locacoes = asList(
				umLocacao()
					.comUsuario(user)
					.atrasado()
					.agora(),
				umLocacao()
					.comUsuario(user2)
					.agora(),
				umLocacao()
					.comUsuario(user3)
					.atrasado()
					.agora());
		
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		service.notificarAtrasos();
		
		verify(email).notificarAtraso(user);
		verify(email, never()).notificarAtraso(user2);
		verify(email).notificarAtraso(user3);
		verifyNoMoreInteractions(email);
	}
	
	@Test
	public void erroNoSPC() throws Exception {
		List <Filme> filme = asList(umFilme().agora());
		
		when(spc.isNegativado(user)).thenThrow(new Exception("Falha no sistema do SPC, ou algo assim"));
		
		exception.expect(Exception.class);
		exception.expectMessage("Falha no SPC");
		
		service.alugarFilme(user, filme);
		
	}
	
	@Test
	public void prorrogarLocacao() {
		Locacao locacao = umLocacao().agora();
		
		service.prorrogarLocacao(locacao, 3);
		
		ArgumentCaptor<Locacao> argCapt = forClass(Locacao.class);
		verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();
		
		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), isHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), isHojeComMaisXDias(3));		
		
	}
}
