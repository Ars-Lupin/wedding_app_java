package model;

import java.time.LocalDate;

/**
 * Classe que representa uma pessoa física, herdando características de Pessoa.
 */
public class PessoaFisica extends Pessoa {

    private String cpf;
    private LocalDate dataNascimento;
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
     */
    public PessoaFisica(String nome, String telefone, Endereco endereco, String cpf, 
                        LocalDate dataNascimento, Financeiro financeiro) {
        super(nome, telefone, endereco);
        setCpf(cpf); // Usa o setter para validar
        this.dataNascimento = dataNascimento;
        this.financeiro = financeiro;
    }

    public String getCpf() {
        return cpf;
    }

    /**
     * Define um novo CPF, garantindo que tenha 11 dígitos numéricos.
     *
     * @param cpf CPF da pessoa física.
     */
    public void setCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido! Deve conter 11 dígitos numéricos.");
        }
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Financeiro getFinanceiro() {
        return financeiro;
    }

    public void setFinanceiro(Financeiro financeiro) {
        this.financeiro = financeiro;
    }

    /**
     * Retorna uma representação textual do objeto.
     *
     * @return String formatada com os dados da pessoa física.
     */
    @Override
    public String toString() {
        return "PessoaFisica{" +
               "nome='" + getNome() + '\'' +
               ", telefone='" + getTelefone() + '\'' +
               ", cpf='" + cpf + '\'' +
               ", dataNascimento=" + dataNascimento +
               ", financeiro=" + financeiro +
               '}';
    }
}
