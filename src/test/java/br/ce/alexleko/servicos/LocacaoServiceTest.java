package br.ce.alexleko.servicos;

import br.ce.alexleko.entidades.Filme;
import br.ce.alexleko.entidades.Locacao;
import br.ce.alexleko.entidades.Usuario;
import br.ce.alexleko.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();



	@Test
	public void teste() {

		// = CENÁRIO =
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);


		// = AÇÃO =
		Locacao locacao = null;
		try {
			locacao = service.alugarFilme(usuario, filme);

		// = VERIFICAÇÃO =
			// Utilizando: Import Static Method
			// Encurta as chamadas de metodo. Ex.: CoreMatchers
			Assert.assertThat(locacao.getValor(), is(CoreMatchers.equalTo(5.0)));
			Assert.assertThat(locacao.getValor(), is(CoreMatchers.not(9.0)));

			Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
			Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// =================
	// = Teste Isolado =
	// =================

	@Test
	public void testeLocacao() throws Exception {

		// = CENÁRIO =
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 1, 5.0);

		// = AÇÃO =
		Locacao locacao = service.alugarFilme(usuario, filme);

		// = VERIFICAÇÃO =
		error.checkThat(locacao.getValor(), is(CoreMatchers.equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));

	}

	// Esperando uma exception - Annotation
	@Test(expected = Exception.class)
	public void testLocacao_filmesemEstoque() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		service.alugarFilme(usuario, filme);
	}


	//Esperando Exception com controle - Try/Catch
	@Test
	public void testLocacao_filmesemEstoque_2() {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		try {
			service.alugarFilme(usuario, filme);

			// caso nao caia na exception... força a falha.
			Assert.fail("Deveria ter lançado uma exceção");

		} catch (Exception e) {
			// Forçando a falha com uma Exception
			Assert.assertThat(e.getMessage(), is("Filme sem estoque"));
		}
	}


	// Esperando Exception com controle - Rule
	@Test
	public void testLocacao_filmesemEstoque_3() throws Exception {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		// Avisa que deverá ser lancado uma exception
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		//acao
		service.alugarFilme(usuario, filme);
	}

}