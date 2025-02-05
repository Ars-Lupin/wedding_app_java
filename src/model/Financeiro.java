package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe que representa um objeto financeiro genérico no sistema.
 * Possui informações como dinheiro guardado na poupança,
 * salário líquido e gastos mensais.
 */
public class Financeiro {

    private BigDecimal dinheiroPoupanca;
    private BigDecimal salarioLiquido;
    private BigDecimal gastosMensais;

    /**
     * Construtor da classe Financeiro.
     *
     * @param dinheiroPoupanca Dinheiro guardado na poupança (não pode ser negativo).
     * @param salarioLiquido   Salário líquido mensal (não pode ser negativo).
     * @param gastosMensais    Gastos mensais da pessoa (não pode ser negativo).
     */
    public Financeiro(double dinheiroPoupanca, double salarioLiquido, double gastosMensais) {
        this.dinheiroPoupanca = validarValor(dinheiroPoupanca, "Dinheiro na poupança");
        this.salarioLiquido = validarValor(salarioLiquido, "Salário líquido");
        this.gastosMensais = validarValor(gastosMensais, "Gastos mensais");
    }

    private BigDecimal validarValor(double valor, String campo) {
        if (valor < 0) {
            throw new IllegalArgumentException(campo + " não pode ser negativo.");
        }
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDinheiroPoupanca() {
        return dinheiroPoupanca;
    }

    public void setDinheiroPoupanca(double dinheiroPoupanca) {
        this.dinheiroPoupanca = validarValor(dinheiroPoupanca, "Dinheiro na poupança");
    }

    public BigDecimal getSalarioLiquido() {
        return salarioLiquido;
    }

    public void setSalarioLiquido(double salarioLiquido) {
        this.salarioLiquido = validarValor(salarioLiquido, "Salário líquido");
    }

    public BigDecimal getGastosMensais() {
        return gastosMensais;
    }

    public void setGastosMensais(double gastosMensais) {
        this.gastosMensais = validarValor(gastosMensais, "Gastos mensais");
    }

    /**
     * Calcula o saldo mensal (dinheiro que sobra ou falta após pagar despesas).
     *
     * @return Saldo mensal.
     */
    public BigDecimal calcularSaldoMensal() {
        return salarioLiquido.subtract(gastosMensais);
    }

    /**
     * Aplica rendimento mensal da poupança (0.5% ao mês).
     * Apenas para testes, talvez haja mudanças futuras.
     */
    public void aplicarRendimento() {
        BigDecimal rendimento = dinheiroPoupanca.multiply(BigDecimal.valueOf(0.005));
        dinheiroPoupanca = dinheiroPoupanca.add(rendimento).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Retorna uma representação textual do objeto financeiro.
     * 
     * @return String formatada com os dados financeiros de uma pessoa.
     */
    @Override
    public String toString() {
        return String.format("Financeiro{ Dinheiro Poupança=%s, Salário Líquido=%s, Gastos Mensais=%s, Saldo Mensal=%s }",
                formatarValor(dinheiroPoupanca), formatarValor(salarioLiquido),
                formatarValor(gastosMensais), formatarValor(calcularSaldoMensal()));
    }

    private String formatarValor(BigDecimal valor) {
        return String.format("R$ %.2f", valor);
    }
}
