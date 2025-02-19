package model;

/**
 * Classe que representa uma pessoa jurídica no sistema.
 */
public class PessoaJuridica extends Pessoa {
    
    private final String cnpj;

    /**
     * Construtor da classe PessoaJuridica.
     *
     * @param nome      Nome da empresa.
     * @param telefone  Telefone de contato.
     * @param endereco  Endereço da empresa.
     * @param cnpj      CNPJ da empresa (14 dígitos numéricos).
     * @param idPessoa  ID da pessoa jurídica.
     */
    public PessoaJuridica(String nome, String telefone, String endereco, String cnpj, String idPessoa) {
        super(nome, telefone, endereco, idPessoa);

        if (cnpj == null || !cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")) {
            throw new IllegalArgumentException("CNPJ inválido! Deve conter exatamente 14 dígitos numéricos.");
        }

        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }

    /**
     * Retorna uma representação textual do objeto (para testes e logs).
     * 
     * @return String formatada com os dados da pessoa jurídica.
     */
    @Override
    public String toString() {
        return String.format("PessoaJuridica{ID='%s', Nome='%s', Telefone='%s', CNPJ='%s', Endereço=%s}",
                getIdPessoa(), getNome(), getTelefone(), cnpj, getEndereco());
    }
}
