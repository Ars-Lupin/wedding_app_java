package model;

/**
 * Classe que representa uma pessoa jurídica no sistema.
 */
public class PessoaJuridica extends Pessoa {
    
    private String cnpj;

    /**
     * Construtor da classe PessoaJuridica.
     *
     * @param nome      Nome da empresa.
     * @param telefone  Telefone de contato.
     * @param endereco  Endereço da empresa.
     * @param cnpj      CNPJ da empresa (14 dígitos numéricos).
     * @param idPessoa  ID da pessoa jurídica.
     */
    public PessoaJuridica(String nome, String telefone, Endereco endereco, String cnpj, String idPessoa) {
        super(nome, telefone, endereco, idPessoa);

        // Verifica se o CNPJ está vazio ou não tem 14 dígitos
        if (cnpj == null || !cnpj.matches("\\d{14}")) {
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
        return "PessoaJuridica{" +
               "nome='" + getNome() + '\'' +
               ", telefone='" + getTelefone() + '\'' +
               ", cnpj='" + cnpj + '\'' +
               ", endereco='" + getEndereco() + '\'' +
                ", idPessoa='" + getIdPessoa() + '\'' +
               '}';
    }
}
