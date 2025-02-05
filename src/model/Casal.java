package model;

import java.math.BigDecimal;

/**
 * Classe que representa um casal no sistema.
 */
public class Casal {

    private PessoaFisica pessoa1;
    private PessoaFisica pessoa2;

    /**
     * Construtor da classe Casal.
     *
     * @param pessoa1 Primeira pessoa do casal.
     * @param pessoa2 Segunda pessoa do casal.
     */
    public Casal(PessoaFisica pessoa1, PessoaFisica pessoa2) {
        if (pessoa1 == null || pessoa2 == null) {
            throw new IllegalArgumentException("Nenhuma das pessoas pode ser nula.");
        }
        if (pessoa1.getCpf().equals(pessoa2.getCpf())) {
            throw new IllegalArgumentException("As duas pessoas devem ter CPFs diferentes.");
        }

        // Garante que os nomes estão em ordem alfabética
        if (pessoa1.getNome().compareToIgnoreCase(pessoa2.getNome()) > 0) {
            this.pessoa1 = pessoa2;
            this.pessoa2 = pessoa1;
        } else {
            this.pessoa1 = pessoa1;
            this.pessoa2 = pessoa2;
        }
    }

    public PessoaFisica getPessoa1() {
        return pessoa1;
    }

    public PessoaFisica getPessoa2() {
        return pessoa2;
    }

    /**
     * Retorna o saldo total do casal (soma dos valores guardados na poupança).
     *
     * @return Saldo total do casal.
     */
    public BigDecimal getSaldoTotal() {
        return pessoa1.getFinanceiro().getDinheiroPoupanca()
                .add(pessoa2.getFinanceiro().getDinheiroPoupanca());
    }

    /**
     * Retorna uma representação textual do casal.
     * 
     * @return String formatada com os dados do casal.
     */
    @Override
    public String toString() {
        return String.format("Casal{Pessoa 1='%s', Pessoa 2='%s', Saldo Total='%s'}",
                pessoa1.getNome(), pessoa2.getNome(), formatarValor(getSaldoTotal()));
    }

    private String formatarValor(BigDecimal valor) {
        return String.format("R$ %.2f", valor);
    }
}
