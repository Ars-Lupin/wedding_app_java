package model;

/**
 * Classe que representa um objeto financeiro genérico 
 * no sistema.
 * Possui informações como dinheiro guardado na poupança,
 * salário liquido e gastos mensais
 */

public class Financeiro {

    private double dinheiroPoupanca;
    private double salarioLiquido;
    private double gastosMensais;

    public Financeiro(double dinheiroPoupanca, double salarioLiquido, double gastosMensais) {
        if (dinheiroPoupanca < 0) {
            throw new IllegalArgumentException("Dinheiro na poupança não pode ser negativo.");
        }
        if (salarioLiquido < 0) {
            throw new IllegalArgumentException("Salário líquido não pode ser negativo.");
        }
        if (gastosMensais < 0) {
            throw new IllegalArgumentException("Gastos mensais não podem ser negativos.");
        }
        this.dinheiroPoupanca = dinheiroPoupanca;
        this.salarioLiquido = salarioLiquido;
        this.gastosMensais = gastosMensais;
    }

    public double getDinheiroPoupanca() {
        return dinheiroPoupanca;
    }

    public void setDinheiroPoupanca(double dinheiroPoupanca) {
        if (dinheiroPoupanca < 0) {
            throw new IllegalArgumentException("Dinheiro na poupança não pode ser negativo.");
        }
        this.dinheiroPoupanca = dinheiroPoupanca;
    }

    public double getSalarioLiquido() {
        return salarioLiquido;
    }

    public void setSalarioLiquido(double salarioLiquido) {
        if (salarioLiquido < 0) {
            throw new IllegalArgumentException("Salário líquido não pode ser negativo.");
        }
        this.salarioLiquido = salarioLiquido;
    }

    public double getGastosMensais() {
        return gastosMensais;
    }

    public void setGastosMensais(double gastosMensais) {
        if (gastosMensais < 0) {
            throw new IllegalArgumentException("Gastos mensais não podem ser negativos.");
        }
        this.gastosMensais = gastosMensais;
    }

    /**
     * Retorna uma representação textual do objeto. (Para testes de sanidade, por exemplo)
     * 
     * @return String formatada com os dados financeiros de uma pessoa fisica.
     */
    @Override
    public String toString() {
        return "Financeiro{" +
               "Dinheiro na poupanca='" + dinheiroPoupanca + '\'' +
               ", Salario liquido='" + salarioLiquido + '\'' +
               ", Gastos mensais='" + gastosMensais + '\'' +
               '}';
    }
}
