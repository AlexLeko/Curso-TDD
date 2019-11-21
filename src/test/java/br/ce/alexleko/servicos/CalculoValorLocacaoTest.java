package br.ce.alexleko.servicos;

import br.ce.alexleko.entidades.Filme;
import br.ce.alexleko.entidades.Locacao;
import br.ce.alexleko.entidades.Usuario;
import br.ce.alexleko.exceptions.FilmeSemEstoqueException;
import br.ce.alexleko.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    // ========================
    // =   DATA DRIVEN TEST   =
    // ========================

    private LocacaoService service;

    // Variaveis utilizadas na construção do objeto do @Parameters.
    // intitulado value = 0;
    @Parameterized.Parameter
    public List<Filme> filmes;

    @Parameterized.Parameter(value = 1)
    public Double valorLocacao;

    @Parameterized.Parameter(value = 2)
    public String cenario;


    @Before
    public void setup() {
        service = new LocacaoService();
    }


    // Filmes Mock por Variaveis
    private static Filme filme_1 = new Filme("Filme 1", 2, 4.0);
    private static Filme filme_2 = new Filme("Filme 2", 2, 4.0);
    private static Filme filme_3 = new Filme("Filme 3", 2, 4.0);
    private static Filme filme_4 = new Filme("Filme 4", 2, 4.0);
    private static Filme filme_5 = new Filme("Filme 5", 2, 4.0);
    private static Filme filme_6 = new Filme("Filme 6", 2, 4.0);
    private static Filme filme_7 = new Filme("Filme 7", 2, 4.0);

    // NAME = variaveis é o valor do @Parameter.
    @Parameterized.Parameters(name = "Cenário: {index} = {2}")
    public static Collection<Object[]> getParametros() {
        // dentro da matriz cada objeto representa um cenario distinto;
        // Objeto do cenario contem seus filmes, valor e a descrição(titulo do cenário... só para exemplo);
        return Arrays.asList(new Object[][] {
            // Cenarios de descontos para ultimo filme conforme quantidade alugada.
            { Arrays.asList(filme_1, filme_2), 8.0, "2 Filmes: Sem Desconto" },
            { Arrays.asList(filme_1, filme_2, filme_3), 11.0, "3 Filmes: 25%" },
            { Arrays.asList(filme_1, filme_2, filme_3, filme_4), 13.0, "4 Filmes: 50%" },
            { Arrays.asList(filme_1, filme_2, filme_3, filme_4, filme_5), 14.0, "5 Filmes: 75%" },
            { Arrays.asList(filme_1, filme_2, filme_3, filme_4, filme_5, filme_6, filme_7), 18.0, "7 Filmes: Sem Desconto" }
        });
    }


    // Executa a bateria de testes, conforme cenários contruidos no @Parameters.
    // Cada cenário executa os testes novamente. Por isso o @Parameters tem que ser STATIC;

    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = new Usuario("Usuario 1");

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        // verificacao
        assertThat(resultado.getValor(), is(valorLocacao));
    }

}
