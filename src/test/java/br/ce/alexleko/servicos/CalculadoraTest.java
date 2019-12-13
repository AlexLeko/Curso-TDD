package br.ce.alexleko.servicos;

import br.ce.alexleko.exceptions.NaoPodeDividirPorZeroException;
import br.ce.alexleko.runners.ParallelRunner;
import org.junit.*;
import org.junit.runner.RunWith;

//@RunWith(ParallelRunner.class)
public class CalculadoraTest {

    public static StringBuffer ordem = new StringBuffer();

    private Calculadora calc;

    @Before
    public void setup() {
        calc = new Calculadora();
        System.out.println("Iniciando...");
        ordem.append("1");
    }

    @After
    public void tearDown() {
        System.out.println("Finalizado!");
    }

    @AfterClass
    public static void tearDownClasss() {
        System.out.println(ordem.toString());
    }



    @Test
    public void deveSomarDoisValores() {
        //cenario
        int a = 5;
        int b = 3;

        //acao
        int resultado = calc.somar(a, b);

        //verificacao
        Assert.assertEquals(8, resultado);
    }

    @Test
    public void deveSubtrairDoisValores() {
        //cenario
        int a = 8;
        int b = 5;

        //acao
        int resultado = calc.subtrair(a, b);

        //verificação
        Assert.assertEquals(3, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
        //cenario
        int a = 6;
        int b = 3;

        //acao
        int resultado = calc.dividir(a, b);

        //verificao
        Assert.assertEquals(2, resultado);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExceptionAoDividirPorZero() throws NaoPodeDividirPorZeroException {
        int a = 10;
        int b = 0;
        calc.dividir(a, b);
    }




}
