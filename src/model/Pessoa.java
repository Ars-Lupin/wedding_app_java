package model;

public class Pessoa {
    
    String nome;
    String telefone;

    public Pessoa(String nome, String telefone)
    {
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }
    
    public String getTelefone() {
        return telefone;
    }
}
