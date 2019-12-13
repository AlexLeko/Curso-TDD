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
import org.powermock.reflect.Whitebox;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static br.ce.alexleko.builders.FilmeBuilder.umFilme;
import static br.ce.alexleko.builders.UsuarioBuilder.umUsuario;
import static br.ce.alexleko.matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
// prepara o ambiente dessa classe de acordo com as classes informadas para receber as modificações do powerMock.
@PrepareForTest({ LocacaoService.class } )
public class LocacaoServiceTest_PowerMock {

	// Injetar os Mocks nesta classe.
	@InjectMocks
	private LocacaoService service;

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

		// SPY - Powermock
		service = PowerMockito.spy(service);

		System.out.println("Iniciando 4");
		CalculadoraTest.ordem.append(4);
	}

	@After
	public void tearDown() {
		System.out.println("Finalizado 4");
	}

	@AfterClass
	public static void tearDownClasss() {
		System.out.println(CalculadoraTest.ordem.toString());
	}


	@Test
	public void deveAlugarFilme() throws FilmeSemEstoqueException, LocadoraException {

		// = CENÁRIO =
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

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


	@Test
	public void naoDeveDevolverFilmeNoDomingo() throws Exception {

		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

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
	public void deveAlugarFilme_SemCalcularValor() throws Exception {
		// utiliza o SPY do PowerMockito instanciado no @Before.
		// Não executa o metodo de calculo do valor.

		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// Mockando um metodo PRIVATE e definindo o valor de retorno.
		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		Assert.assertThat(locacao.getValor(), is(1.0));	// modo normal
		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);	// modo PM
	}

	@Test
	public void deveCalcularValorLocacao() throws Exception {
		// Testar um método Privado

		// - cenario -
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// - ação -
		Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);

		// - verificação -
		Assert.assertThat(valor, is(4.0));
	}

}