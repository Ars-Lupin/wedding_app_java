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
     */
    public PessoaJuridica(String nome, String telefone, Endereco endereco, String cnpj) {
        super(nome, telefone, endereco);
        setCnpj(cnpj); // Usa o setter para validar
    }

    public String getCnpj() {
        return cnpj;
    }

    /**
     * Define um novo CNPJ, garantindo que tenha 14 dígitos numéricos.
     *
     * @param cnpj CNPJ da empresa.
     */
    public void setCnpj(String cnpj) {
        // Verifica se o CNPJ está vazio ou não tem 14 dígitos
        if (cnpj == null || !cnpj.matches("\\d{14}")) {
            throw new IllegalArgumentException("CNPJ inválido! Deve conter exatamente 14 dígitos numéricos.");
        }
        this.cnpj = cnpj;
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
               '}';
    }
}
