package model;

/**
 * Classe que representa uma loja no sistema.
 */
public class Loja extends PessoaJuridica {

    public Loja (String nome, String telefone, Endereco endereco, String cnpj) {
        super(nome, telefone, endereco, cnpj);
    }
}
