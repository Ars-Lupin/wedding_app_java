package model;

import java.time.LocalDate;

/**
 * Classe que representa uma pessoa física, herdando características de Pessoa.
 */
public class PessoaFisica extends Pessoa {

    private final String cpf;
    private final LocalDate dataNascimento;
    private Financeiro financeiro;

    /**
     * Construtor da classe PessoaFisica.
     *
     * @param nome           Nome da pessoa.
     * @param telefone       Telefone da pessoa.
     * @param endereco       Endereço da pessoa.
     * @param cpf            CPF da pessoa (11 dígitos).
     * @param dataNascimento Data de nascimento da pessoa.
     * @param financeiro     Informações financeiras da pessoa.
     * @param idPessoa       Identificador único da pessoa.
     */
    public PessoaFisica(String nome, String telefone, Endereco endereco, String cpf, 
                        LocalDate dataNascimento, Financeiro financeiro, String idPessoa) {         
        super(nome, telefone, endereco, idPessoa);

        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido! Deve conter 11 dígitos numéricos.");
        }
        if (dataNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula.");
        }
        if (financeiro == null) {
            throw new IllegalArgumentException("Informações financeiras não podem ser nulas.");
        }

        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.financeiro = financeiro;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Financeiro getFinanceiro() {
        return financeiro;
    }

    public void setFinanceiro(Financeiro financeiro) {
        if (financeiro == null) {
            throw new IllegalArgumentException("Informações financeiras não podem ser nulas.");
        }
        this.financeiro = financeiro;
    }

    /**
     * Retorna uma representação textual do objeto.
     *
     * @return String formatada com os dados da pessoa física.
     */
    @Override
    public String toString() {
        return String.format("PessoaFisica{ID='%s', Nome='%s', Telefone='%s', CPF='%s', DataNascimento=%s, Endereco=%s, Financeiro=%s}",
                getIdPessoa(), getNome(), getTelefone(), cpf, dataNascimento, getEndereco(), financeiro);
    }
}
