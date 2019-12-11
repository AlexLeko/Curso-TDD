package br.ce.alexleko.servicos;

import br.ce.alexleko.dao.LocacaoDAO;
import br.ce.alexleko.entidades.Filme;
import br.ce.alexleko.entidades.Locacao;
import br.ce.alexleko.entidades.Usuario;
import br.ce.alexleko.exceptions.FilmeSemEstoqueException;
import br.ce.alexleko.exceptions.LocadoraException;
import br.ce.alexleko.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.alexleko.builders.FilmeBuilder.umFilme;
import static br.ce.alexleko.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.alexleko.builders.LocacaoBuilder.*;
import static br.ce.alexleko.builders.UsuarioBuilder.umUsuario;
import static br.ce.alexleko.matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
// prepara o ambiente dessa classe de acordo com as classes informadas para receber as modificações do powerMock.
@PrepareForTest({ LocacaoService.class } )
public class LocacaoServiceTest {

	// Injetar os Mocks nesta classe.
	@InjectMocks
	private LocacaoService service;

	// o JUnit sempre inicialia a variavel, para um resultado não afetar outro teste
	// com um variavel static o valor passa para o escopo de classe e não muda.
	//private static int contador = 0;

	// Mock por Annotations
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
		// inicializa os Mocks
		MockitoAnnotations.initMocks(this);

//		service = new LocacaoService();
//
//		// Instancia Fake com Mockito
//		dao = Mockito.mock(LocacaoDAO.class);
//		service.setLocacaoDAO(dao);
//
//		// Mock do SPC
//		spc = Mockito.mock(SPCService.class);
//		service.setSpcService(spc);
//
//		// Mock Email
//		email = Mockito.mock(EmailService.class);
//		service.setEmailService(email);
	}

//	@After
//	public void tearDown() {
//		System.out.println("After");
//	}

//	@BeforeClass
//	public static void setupClass() {
//		System.out.println("Before Class");
//	}

//	@AfterClass
//	public static void tearDownClass() {
//		System.out.println("After Class");
//	}




//	@Test
//	public void teste() {
//
//		// = CENÁRIO =
//		LocacaoService service = new LocacaoService();
//		Usuario usuario = new Usuario("Usuario 1");
//		Filme filme = new Filme("Filme 1", 2, 5.0);
//
//
//		// = AÇÃO =
//		Locacao locacao = null;
//		try {
//			locacao = service.alugarFilme(usuario, filme);
//
//		// = VERIFICAÇÃO =
//			// Utilizando: Import Static Method
//			// Encurta as chamadas de metodo. Ex.: CoreMatchers
//			assertThat(locacao.getValor(), is(CoreMatchers.equalTo(5.0)));
//			assertThat(locacao.getValor(), is(CoreMatchers.not(9.0)));
//
//			assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
//			assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	// =================
	// = Teste Isolado =
	// =================

	@Test
	public void deveAlugarFilme() throws Exception {

		// = CENÁRIO =
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

		// Assumptions
		// Define que se for SATURDAY esse teste NÃO deve ser realizado.
		//Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// = Power Mockito =
		// Deve definir um novo Runner na classe. @RunWith(PowerMockRunner.class)
		// Quando executar uma nova instancia da classe Date, sem argumentos, retorna a data mockada. (SABADO)
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(11, 12, 2019));

		// Se estiver usando o Calendar ou uma classe STATIC
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 11);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.YEAR, 2019);

		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

		// = AÇÃO =
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// = VERIFICAÇÃO =
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));

		// Matchers
//		error.checkThat(locacao.getDataLocacao(), ehHoje());
//		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));

		// valida a data de acordo com a data mockada pelo PowerMock.
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData( 11, 12, 2019)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData( 12, 12, 2019)), is(true));
	}

	// Esperando uma exception - Annotation
	// Necessario garantir que essa exception esta vindo por apenas um motivo.
	// se garantir é o melhor modo a implementar
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		service.alugarFilme(usuario, filmes);
	}


	// Esperando Exception com controle - Try/Catch
	// só nesta forma o fluxo continua após as verificações.
//	@Test
//	public void testLocacao_filmesemEstoque_2() {
//		LocacaoService service = new LocacaoService();
//		Usuario usuario = new Usuario("Usuario 1");
//		Filme filme = new Filme("Filme 1", 0, 4.0);
//
//		try {
//			service.alugarFilme(usuario, filme);
//
//			// verificação
//
//			// caso nao caia na exception... força a falha.
//			Assert.fail("Deveria ter lançado uma exceção");
//
//		} catch (Exception e) {
//			// Forçando a falha com uma Exception
//			assertThat(e.getMessage(), is("Filme sem estoque"));
//		}
//	}


	// Esperando Exception com controle - Rule
//	@Test
//	public void testLocacao_filmesemEstoque_3() throws Exception {
//		//cenario
//		LocacaoService service = new LocacaoService();
//		Usuario usuario = new Usuario("Usuario 1");
//		Filme filme = new Filme("Filme 1", 0, 5.0);
//
//		// Avisa que deverá ser lancado uma exception
//		exception.expect(Exception.class);
//		exception.expectMessage("Filme sem estoque");
//
//		//acao
//		service.alugarFilme(usuario, filme);
//	}



	// TRATATIVA COM EXCEPTIONS PERSONALIZADAS

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {

		// cenario
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// acao
		try {
			service.alugarFilme(null, filmes);

			// verificao
			Assert.fail();

		} catch (LocadoraException e) {
			// verificao/home/s2it_asantos/.m2/repository/junit/junit/4.12
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}


	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

		//cenario
		Usuario usuario = umUsuario().agora();

		// verificacao
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// acao
		service.alugarFilme(usuario, null);
	}

	// =============  DATA DRIVEN TEST  ====================
	// Testes Parametrizado - classe: CalculoValorLocacaoTest;


//	@Test
//	public void devePagar75porcentoNoTerceiroFilme() throws FilmeSemEstoqueException, LocadoraException {
//		Usuario usuario = new Usuario("Usuario 1");
//		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0),
//											new Filme("Filme 2", 2, 4.0),
//											new Filme("Filme 3", 2, 4.0));
//
//		Locacao resultado = service.alugarFilme(usuario, filmes);
//
//		// (4 + 4 + 3) -> 25% = 11
//		assertThat(resultado.getValor(), is(11.0));
//	}
//
//	@Test
//	public void devePagar50porcentoNoQuartoFilme() throws FilmeSemEstoqueException, LocadoraException {
//		Usuario usuario = new Usuario("Usuario 1");
//		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0),
//											new Filme("Filme 2", 2, 4.0),
//											new Filme("Filme 3", 2, 4.0),
//											new Filme("Filme 4", 2, 4.0));
//
//		Locacao resultado = service.alugarFilme(usuario, filmes);
//
//		// (4 + 4 + 3 + 2) -> 50% = 13
//		assertThat(resultado.getValor(), is(13.0));
//	}
//
//	@Test
//	public void devePagar25porcentoNoQuintoFilme() throws FilmeSemEstoqueException, LocadoraException {
//		Usuario usuario = new Usuario("Usuario 1");
//		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0),
//											new Filme("Filme 2", 2, 4.0),
//											new Filme("Filme 3", 2, 4.0),
//											new Filme("Filme 4", 2, 4.0),
//											new Filme("Filme 5", 2, 4.0));
//
//		Locacao resultado = service.alugarFilme(usuario, filmes);
//
//		// (4 + 4 + 3 + 2 + 1) -> 75% = 14
//		assertThat(resultado.getValor(), is(14.0));
//	}
//
//	@Test
//	public void devePagarZeroNoSextoFilme() throws FilmeSemEstoqueException, LocadoraException {
//		Usuario usuario = new Usuario("Usuario 1");
//		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0),
//				new Filme("Filme 2", 2, 4.0),
//				new Filme("Filme 3", 2, 4.0),
//				new Filme("Filme 4", 2, 4.0),
//				new Filme("Filme 5", 2, 4.0),
//				new Filme("Filme 6", 2, 4.0));
//
//		Locacao resultado = service.alugarFilme(usuario, filmes);
//
//		// (4 + 4 + 3 + 2 + 1 + 0) -> 100% = 14
//		assertThat(resultado.getValor(), is(14.0));
//	}

	// =============  DATA DRIVEN TEST  ====================

	@Test
//	@Ignore		// Ignora este teste
	public void naoDeveDevolverFilmeNoDomingo() throws Exception {

		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// = Assumptions =
		// Define que se for SATURDAY esse teste deve ser realizado.
		// Se não for sabado este teste é ignorado, mas não dará erro.
		//Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// = Power Mockito =
		// Deve definir um novo Runner na classe. @RunWith(PowerMockRunner.class)
		// Deve preparar as classes - @PrepareForTest({LocacaoService.class, DataUtils.class})
		// Quando executar uma nova instancia da classe Date, sem argumentos, retorna a data mockada. (SABADO)
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(14, 12, 2019));

		// expectativa - static
		// Se estiver usando o Calendar ou uma classe STATIC
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 14);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.YEAR, 2019);

		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

		Locacao retorno = service.alugarFilme(usuario, filmes);

//		boolean isSegunda =  DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
//		Assert.assertTrue(isSegunda);

		// Matcher
//		assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

		// verificar se o construtor da classe foi chamado.
		// espera que invoque 2X o construtor da classe Date.
		//PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();

		// Verificar se o método static foi chamado.
		PowerMockito.verifyStatic(Mockito.times(2));
		Calendar.getInstance();
	}

	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
//		Usuario usuario2 = umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// Mockito define os valores Default da tipagem da variavel;
		// Define o valor desejado (diferente de Default) para a chamada do metodo;
		when(spc.possuiNegativacao(usuario)).thenReturn(true);
		// Validando para qualquer Usuario, deixando mais generico.
		//when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		// Espero que lance essa exception
		// quando se lança a exception nao tem mais controle para verificacoes,
		// por isso refatorei com o modo try/catch.s

//		exception.expect(LocadoraException.class);
//		exception.expectMessage("Usuário Negativado");

		// acao
		try {
			service.alugarFilme(usuario, filmes);

		//verificacao
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário Negativado"));
		}

		// caso crie usuario2, nao esta definido o retorno, o Mockito criou o retorno para o usuario.
//		service.alugarFilme(usuario2, filmes);

		verify(spc).possuiNegativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = umUsuario().agora();
		// teste erro
		Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Atrasado").agora();

		List<Locacao> locacoes = Arrays.asList(
				umLocacao()
					.comUsuario(usuario).atrasado().agora(),
				umLocacao()
					.comUsuario(usuario2).agora(),
				umLocacao()
					.comUsuario(usuario3).atrasado().agora(),
				umLocacao()
					.comUsuario(usuario3).atrasado().agora()
		);

		//expectativa
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		//acao
		service.notificarAtrasos();

		//verificacao
		// verifica se no EmailService chamou o notificar, para este usuario, com sucesso.
		verify(email).notificarAtraso(usuario);
		//verify(email).notificarAtraso(usuario3);

		// verifica no email que o notificar NUNCA ocorreu.
		verify(email, never()).notificarAtraso(usuario2);

		// garante que nenhum outro e-mail foi enviado, além dos VERIFY que listei.
		// caso deixe de verificar algum elemento, não vai passar o test.
		//verifyNoMoreInteractions(email);

		// ===  somente para conhecimento  ===

		// garante que não ocorreu nenhum acesso ao SPC;
		verifyZeroInteractions(spc);

		// verifica se enviou mais de uma vez no mesmo e-mail
		verify(email, times(2)).notificarAtraso(usuario3);	// enviou 2x no mesmo e-mail
		verify(email, Mockito.atMost(5)).notificarAtraso(usuario3);		// No Maximo
		verify(email, Mockito.atLeastOnce()).notificarAtraso(usuario3);		// No minimo uma vez

		// verifica quantos email foram lançados independente de qual usuario.
		verify(email, times(3)).notificarAtraso(Mockito.any(Usuario.class));

	}

	// ====================================
	// Quando o teste espera uma Exception.

	@Test
	public void deveTratarErroSPC() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// expectativa
		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha Catastrófica"));

		// tratar
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC, tente novamente!");

		// acao
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void deveProrrogarUmaLocacao() {
		// cenario
		Locacao locacao = umLocacao().agora();

		// acao
		service.prorrogarLocacao(locacao, 3);

		// verificacao
		// Cria um objeto para capturar os valores criados no SALVAR e retorna-lo.
		ArgumentCaptor<Locacao> argCaptor = ArgumentCaptor.forClass(Locacao.class);
		verify(dao).salvar(argCaptor.capture());
		Locacao locacaoRetornada = argCaptor.getValue();

		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
	}

}