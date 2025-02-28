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
     * @param idPessoa  Identificador da loja (mesmo que PessoaJuridica).
     */
    public Loja(String nome, String telefone, String endereco, String cnpj, String idPessoa) {
        super(nome, telefone, endereco, cnpj, idPessoa); // Apenas chama o construtor da superclasse
    }

    /**
     * Retorna uma representação textual da loja.
     *
     * @return String formatada com os dados da loja.
     */
    @Override
    public String toString() {
        return String.format("Loja{ID='%s', Nome='%s', Telefone='%s', CNPJ='%s', Endereço=%s}",
                getIdPessoa(), getNome(), getTelefone(), getCnpj(), getEndereco());
    }
}
