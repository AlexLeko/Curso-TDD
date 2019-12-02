package br.ce.alexleko.dao;

import br.ce.alexleko.entidades.Locacao;

import java.util.List;

public interface LocacaoDAO {

    void salvar(Locacao locacao);

    List<Locacao> obterLocacoesPendentes();

}
