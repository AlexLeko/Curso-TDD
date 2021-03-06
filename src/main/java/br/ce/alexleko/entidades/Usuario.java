package br.ce.alexleko.entidades;

public class Usuario {

	private String nome;
	
	public Usuario() {}
	
	public Usuario(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Usuario usuario = (Usuario) o;

		return nome != null ? nome.equals(usuario.nome) : usuario.nome == null;
	}

	@Override
	public int hashCode() {
		return nome != null ? nome.hashCode() : 0;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Usuario{");
		sb.append("nome='").append(nome).append('\'');
		sb.append('}');
		return sb.toString();
	}
}