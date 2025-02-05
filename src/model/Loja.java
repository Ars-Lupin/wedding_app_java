package model;

/**
 * Classe que representa uma loja no sistema.
 * A loja é sempre uma pessoa jurídica.
 */
public class Loja extends PessoaJuridica {

    /**
     * Construtor da classe Loja.
     *
     * @param nome      Nome da loja.
     * @param telefone  Telefone de contato da loja.
     * @param endereco  Endereço da loja.
     * @param cnpj      CNPJ da loja (14 dígitos numéricos).
     */
    public Loja(String nome, String telefone, Endereco endereco, String cnpj) {
        super(nome, telefone, endereco, cnpj);
    }

    /**
     * Retorna uma representação textual da loja.
     *
     * @return String formatada com os dados da loja.
     */
    @Override
    public String toString() {
        return "Loja{" +
               "nome='" + getNome() + '\'' +
               ", telefone='" + getTelefone() + '\'' +
               ", cnpj='" + getCnpj() + '\'' +
               ", endereco='" + getEndereco() + '\'' +
               '}';
    }
}
