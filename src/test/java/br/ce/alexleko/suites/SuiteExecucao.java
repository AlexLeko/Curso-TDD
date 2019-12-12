package br.ce.alexleko.suites;

import br.ce.alexleko.servicos.CalculadoraTest;
import br.ce.alexleko.servicos.CalculoValorLocacaoTest;
import br.ce.alexleko.servicos.LocacaoService;
import br.ce.alexleko.servicos.LocacaoServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@RunWith(Suite.class)
@Suite.SuiteClasses({
    // Teste que desejo executer nesta suite;
//        CalculadoraTest.class,
        CalculoValorLocacaoTest.class,
        LocacaoServiceTest.class
})
public class SuiteExecucao {
    // Essa classe só serve para identificar qual os testes estão nesta suite.
    // Bom lugar para declarar os @BeforeClass e @AfterClass
    // Tem problemas com ferramenta de Integração Continua, pois ela executa todos os testes que existir.
    // |-> com isso executa os testes da suite 2 vezes.

//    @BeforeClass
//    public static void before() {
//        // Ex.: Criar um mock de banco de dados.
//        System.out.println("= BEFORE =");
//    }

//    @AfterClass
//    public static void after() {
//        System.out.println("= AFTER =");
//    }

}
