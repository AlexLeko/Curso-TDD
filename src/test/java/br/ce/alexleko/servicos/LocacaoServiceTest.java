package br.ce.alexleko.servicos;

import br.ce.alexleko.entidades.Filme;
import br.ce.alexleko.entidades.Locacao;
import br.ce.alexleko.entidades.Usuario;
import br.ce.alexleko.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

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
		Assert.assertEquals(5.0, locacao.getValor(),  0.01);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}

}