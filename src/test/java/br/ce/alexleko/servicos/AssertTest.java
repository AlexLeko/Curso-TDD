package br.ce.alexleko.servicos;

import br.ce.alexleko.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void assertivas() {

        // = BOOLEAN =
        Assert.assertTrue(true);
        Assert.assertFalse(false);


        // = EQUALS =

        // Tipos primitivos
        Assert.assertEquals(1, 1);

        // para Float / Double é necessario usar o valor Delta.
        // Delta é o valor da margem de erro na comparação dos valores.
        Assert.assertEquals(0.51234, 0.512, 0.001);

        // IMPORTANTE: Dizimas periodicas infinitas.
        Assert.assertEquals(Math.PI, 3.14, 0.01);

        // Tipos Primitivos (Wrappers)
        int i = 5;
        Integer ii = 5;

        // Necessario Converter Primitivo para Objeto
        Assert.assertEquals(Integer.valueOf(i), ii);
        // OU
        // Necessario Converter Objeto para Primitivo
        Assert.assertEquals(i, ii.intValue());


        // = STRING =

        Assert.assertEquals("bola", "bola");
        Assert.assertNotEquals("bola", "tenis");

        // Case Sensitive
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));

        //Radical
        Assert.assertTrue("bola".startsWith("bo"));


        // = EQUALS PARA OBJETO =
        // Necessario ter [Equals and HashCode] no objeto

        Usuario us1 = new Usuario("Fulano");
        Usuario us2 = new Usuario("Fulano");
        Usuario us3 = null;

        // Objeto com equals por nome iguais
        Assert.assertEquals(us1, us2);

        // Objeto da mesma instancia
        Assert.assertSame(us2, us2);
        Assert.assertNotSame(us1, us2);

        // Null
        Assert.assertNull(us3);
        Assert.assertNotNull(us1);


        // = MENSAGEM DE ERRO NA EXCEPTION =
        //Assert.assertEquals("Erro de comparação: ", 1, 2);


    }

}
