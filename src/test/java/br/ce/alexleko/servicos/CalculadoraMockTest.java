package br.ce.alexleko.servicos;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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

        // Capturar Argumentos
        ArgumentCaptor<Integer> argCaptor = ArgumentCaptor.forClass(Integer.class);

        // caso precise fixar o valor de um parametro.
        Mockito.when(calc.somar(argCaptor.capture(), argCaptor.capture() )).thenReturn(5);

        // quando definido, qualquer soma diferente dos matchers acima, tera o retorno Default do tipo.
        Assert.assertEquals(5, calc.somar(1,1000));

        System.out.println(argCaptor.getAllValues());
    }
}
