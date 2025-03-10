package model;

import java.time.LocalDate;

import exception.DataInconsistencyException;

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
    public PessoaFisica(String nome, String telefone, String endereco, String cpf, 
                        LocalDate dataNascimento, Financeiro financeiro, String idPessoa) throws DataInconsistencyException {         
        super(nome, telefone, endereco, idPessoa); // Chama o construtor da superclasse

        // CPF deve ser no formato 000.000.000-00
        if (cpf == null || !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new DataInconsistencyException("CPF inválido! Deve conter 11 dígitos numéricos.");
        }
        if (dataNascimento == null) {
            throw new DataInconsistencyException("Data de nascimento da pessoa física não pode ser nula.");
        }
        if (financeiro == null) {
            throw new DataInconsistencyException("Informações financeiras da pessoa física não podem ser nulas.");
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

    public void setFinanceiro(Financeiro financeiro) throws DataInconsistencyException {
        if (financeiro == null) {
            throw new DataInconsistencyException("Informações financeiras não podem ser nulas.");
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
