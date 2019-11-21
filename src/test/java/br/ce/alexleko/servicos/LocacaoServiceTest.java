package br.ce.alexleko.servicos;

import br.ce.alexleko.entidades.Filme;
import br.ce.alexleko.entidades.Locacao;
import br.ce.alexleko.entidades.Usuario;
import br.ce.alexleko.exceptions.FilmeSemEstoqueException;
import br.ce.alexleko.exceptions.LocadoraException;
import br.ce.alexleko.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LocacaoServiceTest {

	private LocacaoService service;

	// exercicio

	// o JUnit sempre inicialia a variavel, para um resultado não afetar outro teste
	// com um variavel static o valor passa para o escopo de classe e não muda.
	//private static int contador = 0;


	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();


	@Before
	public void setup() {
		service = new LocacaoService();
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

		// Assumptions
		// Define que se for SATURDAY esse teste NÃO deve ser realizado.
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// = CENÁRIO =
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

		// = AÇÃO =
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// = VERIFICAÇÃO =
		error.checkThat(locacao.getValor(), is(CoreMatchers.equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));

	}

	// Esperando uma exception - Annotation
	// Necessario garantir que essa exception esta vindo por apenas um motivo.
	// se garantir é o melhor modo a implementar
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));


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
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

		// acao
		try {
			service.alugarFilme(null, filmes);

			// verificao
			Assert.fail();

		} catch (LocadoraException e) {
			// verificao
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}


	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

		//cenario
		Usuario usuario = new Usuario("Usuario 1");

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
	public void naoDeveDevolverFilmeNoDomingo() throws FilmeSemEstoqueException, LocadoraException {

		// Assumptions
		// Define que se for SATURDAY esse teste deve ser realizado.
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));


		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

		Locacao retorno = service.alugarFilme(usuario, filmes);

		boolean isSegunda =  DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);

		Assert.assertTrue(isSegunda);
	}




}