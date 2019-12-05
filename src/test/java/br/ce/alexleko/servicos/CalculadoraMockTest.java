package br.ce.alexleko.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculadoraMockTest {

    // = SPY =

    // Não executa de Interface;
//    @Spy
//    private EmailService email;

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora calcSpy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Diferença de SPY / MOCK

    @Test
    public void devoMostrarDiferencaEntreMockSpy() {
        // = MOCK =
        // quando ele realiza uma espectativa, ele consegue retornar o valor esperado.
        // Qualquer outro valor diferente do descrito na expectativa, retornara o valor default do tipo passado.
        Mockito.when(calcMock.somar(1,2)).thenReturn(8); // expectativa;
        System.out.println("MOCK: " + calcMock.somar(1,2)); // retorna 8;
        System.out.println("MOCK Dif: " + calcMock.somar(1,5)); // retorna 0;

        // Executar o metodo sem expectativa estar igual ou definida;
        Mockito.when(calcMock.somar(1,2)).thenCallRealMethod();

        // Para Mock se a chamada é VOID ele nem executa, mesmo sem expectativa definida.
        System.out.println("Mock Calc:");
        calcMock.imprime();


        // = SPY =
        // No cenário normal ele realiza o processo e retorno a expectativa, igual o Mock;
        // Porém, quando os valores forem diferente da expectativa, ele executa normalmente o metodo, e retorna o valor normal.
        // Executa a chamada ao metodo, pelo menos uma vez, e depois indentifica o retorno definido.
        Mockito.when(calcSpy.somar(1,2)).thenReturn(8); // expectativa;
        System.out.println("SPY: " + calcSpy.somar(1,2)); // retorna 3;
        System.out.println("SPY Dif: " + calcSpy.somar(1,5)); // retorna 6;

        // Dessa forma não executa a chamada ao somar nenhuma vez e ja sabe qual o retorno.
        Mockito.doReturn(8).when(calcSpy).somar(1,2);

        // Para SPY ele sempre executara o metodo, sem expectativa definida.
        System.out.println("Spy Calc");
        calcSpy.imprime();

        // Forçar que SPY não exectute, ou seja um VOID;
        Mockito.doNothing().when(calcSpy).imprime();
    }


    // = Matchers =
    // Se um metodo que eu possuir tiver mais de um parametro,
    // caso utiliza Matchers em um parametro tera que utilizar em todos.

    @Test
    public void teste() {
        Calculadora calc = Mockito.mock(Calculadora.class);

        // expectativa Matcher
        Mockito.when(calc.somar(Mockito.anyInt(), Mockito.anyInt() )).thenReturn(5);

        // Capturar Argumentos
        ArgumentCaptor<Integer> argCaptor = ArgumentCaptor.forClass(Integer.class);

        // caso precise fixar o valor de um parametro.
        Mockito.when(calc.somar(argCaptor.capture(), argCaptor.capture() )).thenReturn(5);

        // quando definido, qualquer soma diferente dos matchers acima, tera o retorno Default do tipo.
        Assert.assertEquals(5, calc.somar(1,1000));

        System.out.println(argCaptor.getAllValues());
    }
}
