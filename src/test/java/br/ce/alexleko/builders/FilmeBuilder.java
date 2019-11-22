package br.ce.alexleko.builders;

import br.ce.alexleko.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    private FilmeBuilder() {}

    public static FilmeBuilder umFilme() {
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();
        builder.filme.setNome("Filme 1");
        builder.filme.setEstoque(2);
        builder.filme.setPrecoLocacao(4.0);

        return builder;
    }

    public static FilmeBuilder umFilmeSemEstoque() {
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();
        builder.filme.setNome("Filme 1");
        builder.filme.setEstoque(0);
        builder.filme.setPrecoLocacao(4.0);

        return builder;
    }

    public Filme agora() {
        return filme;
    }

    public FilmeBuilder semEstoque() {
        filme.setEstoque(0);
        return this;
    }

    public FilmeBuilder comValor(Double valor) {
        filme.setPrecoLocacao(valor);
        return this;
    }

}
