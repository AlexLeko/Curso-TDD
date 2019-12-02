package br.ce.alexleko.servicos;

import br.ce.alexleko.entidades.Usuario;

public interface EmailService {

    void notificarAtraso(Usuario usuario);

}
