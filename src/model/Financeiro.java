package model;

/**
 * Classe que representa um objeto financeiro genérico no sistema.
 * Possui informações como dinheiro guardado na poupança,
 * salário líquido e gastos mensais.
 */
public class Financeiro {

    private double dinheiroPoupanca;
    private double salarioLiquido;
    private double gastosMensais;

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

    private double validarValor(double valor, String campo) {
        if (valor < 0) {
            throw new IllegalArgumentException(campo + " não pode ser negativo.");
        }
        return valor;
    }

    public double getDinheiroPoupanca() {
        return dinheiroPoupanca;
    }

    public void setDinheiroPoupanca(double dinheiroPoupanca) {
        this.dinheiroPoupanca = validarValor(dinheiroPoupanca, "Dinheiro na poupança");
    }

    public double getSalarioLiquido() {
        return salarioLiquido;
    }

    public void setSalarioLiquido(double salarioLiquido) {
        this.salarioLiquido = validarValor(salarioLiquido, "Salário líquido");
    }

    public double getGastosMensais() {
        return gastosMensais;
    }

    public void setGastosMensais(double gastosMensais) {
        this.gastosMensais = validarValor(gastosMensais, "Gastos mensais");
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
                formatarValor(gastosMensais), formatarValor(salarioLiquido - gastosMensais));
    }

    private String formatarValor(double valor) {
        return String.format("R$ %.2f", valor);
    }
}
