package br.ce.alexleko.servicos;

import br.ce.alexleko.entidades.Usuario;

public interface SPCService {

    // Pode lançar uma exception, Para Tratar no Test.
    boolean possuiNegativacao (Usuario usuario) throws Exception;

}
