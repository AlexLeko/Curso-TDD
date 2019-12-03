package br.ce.alexleko.servicos;

import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {

    // = Matchers =
    // Se um metodo que eu possuir tiver mais de um parametro,
    // caso utiliza Matchers em um parametro tera que utilizar em todos.

    @Test
    public void teste() {
        Calculadora calc = Mockito.mock(Calculadora.class);

        // expectativa Matcher
        Mockito.when(calc.somar(Mockito.anyInt(), Mockito.anyInt() )).thenReturn(5);

        // caso precise fixar o valor de um parametro.
        Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt() )).thenReturn(5);

        // quando definido, qualquer soma diferente dos matchers acima, tera o retorno Default do tipo.
        System.out.println(calc.somar(1,2));
    }
}
