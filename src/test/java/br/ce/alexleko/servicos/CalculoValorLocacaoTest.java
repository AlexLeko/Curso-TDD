package br.ce.alexleko.servicos;

import br.ce.alexleko.dao.LocacaoDAO;
import br.ce.alexleko.entidades.Filme;
import br.ce.alexleko.entidades.Locacao;
import br.ce.alexleko.entidades.Usuario;
import br.ce.alexleko.exceptions.FilmeSemEstoqueException;
import br.ce.alexleko.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static br.ce.alexleko.builders.FilmeBuilder.umFilme;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    // ========================
    // =   DATA DRIVEN TEST   =
    // ========================

    @InjectMocks
    private LocacaoService service;

    @Mock
    private LocacaoDAO dao;
    @Mock
    private SPCService spc;


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
        MockitoAnnotations.initMocks(this);

//        service = new LocacaoService();
//
//        // Instancia Fake com Mockito
//        LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
//        service.setLocacaoDAO(dao);
//
//        // Mock do SPC
//        SPCService spc = Mockito.mock(SPCService.class);
//        service.setSpcService(spc);
    }


    // Filmes Mock por Variaveis
    private static Filme filme_1 = umFilme().agora();
    private static Filme filme_2 = umFilme().agora();
    private static Filme filme_3 = umFilme().agora();
    private static Filme filme_4 = umFilme().agora();
    private static Filme filme_5 = umFilme().agora();
    private static Filme filme_6 = umFilme().agora();
    private static Filme filme_7 = umFilme().agora();

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

        System.out.println("<(!)>");
    }

}
