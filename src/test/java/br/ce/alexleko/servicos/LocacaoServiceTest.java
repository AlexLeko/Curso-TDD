package br.ce.alexleko.servicos;

import br.ce.alexleko.entidades.Filme;
import br.ce.alexleko.entidades.Locacao;
import br.ce.alexleko.entidades.Usuario;
import br.ce.alexleko.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;

public class LocacaoServiceTest {
	
	@Test
	public void teste() {

		// = CENÁRIO =
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);


		// = AÇÃO =
		Locacao locacao = service.alugarFilme(usuario, filme);


		// = VERIFICAÇÃO =

		// Utilizando: Import Static Method
		// Encurta as chamadas de metodo. Ex.: CoreMatchers
		Assert.assertThat(locacao.getValor(), is(CoreMatchers.equalTo(5.0)));
		Assert.assertThat(locacao.getValor(), is(CoreMatchers.not(9.0)));

		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
	}

}