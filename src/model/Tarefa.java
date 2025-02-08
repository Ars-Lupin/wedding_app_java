package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe que representa uma tarefa associada à organização de um lar.
 */
public class Tarefa {
    
    private final String idTarefa;
    private final String idLar; // Pode ser null
    private final String idPrestador;
    private final LocalDate dataInicio;
    private final int prazoEntrega;
    private final BigDecimal valorPrestador;
    private final int numParcelas;
    private Compra compra; // Pode ser null

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor da classe Tarefa.
     *
     * @param idTarefa       Identificador único da tarefa (32 dígitos).
     * @param idLar          Identificador do lar associado (pode ser null).
     * @param idPrestador    Identificador do prestador de serviço.
     * @param dataInicio     Data de início da tarefa.
     * @param prazoEntrega   Número de dias para conclusão.
     * @param valorPrestador Valor cobrado pelo prestador.
     * @param numParcelas    Número de parcelas do pagamento.
     * @param compra         Compra de materiais associada à tarefa (pode ser null).
     */
    public Tarefa(String idTarefa, String idLar, String idPrestador, LocalDate dataInicio,
                  int prazoEntrega, BigDecimal valorPrestador, int numParcelas, Compra compra) {
        
        if (idTarefa == null || !idTarefa.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID da tarefa deve ter exatamente 32 dígitos numéricos.");
        }
        if (idLar != null && !idLar.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID do lar deve ter exatamente 32 dígitos numéricos.");
        }
        if (idPrestador == null || !idPrestador.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID do prestador deve ter exatamente 32 dígitos numéricos.");
        }
        if (dataInicio == null) {
            throw new IllegalArgumentException("A data de início não pode ser nula.");
        }
        if (prazoEntrega <= 0) {
            throw new IllegalArgumentException("O prazo de entrega deve ser um número positivo.");
        }
        if (valorPrestador == null || valorPrestador.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor do prestador não pode ser negativo.");
        }
        if (numParcelas <= 0) {
            throw new IllegalArgumentException("O número de parcelas deve ser maior que zero.");
        }

        this.idTarefa = idTarefa;
        this.idLar = idLar;
        this.idPrestador = idPrestador;
        this.dataInicio = dataInicio;
        this.prazoEntrega = prazoEntrega;
        this.valorPrestador = valorPrestador;
        this.numParcelas = numParcelas;
        this.compra = compra; // Pode ser null
    }

    public String getIdTarefa() {
        return idTarefa;
    }

    public String getIdLar() {
        return idLar;
    }

    public String getIdPrestador() {
        return idPrestador;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public int getPrazoEntrega() {
        return prazoEntrega;
    }

    public LocalDate getDataEntrega() {
        return dataInicio.plusDays(prazoEntrega);
    }

    public BigDecimal getValorPrestador() {
        return valorPrestador;
    }

    public int getNumParcelas() {
        return numParcelas;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    /**
     * Calcula o valor de cada parcela do prestador de serviço.
     */
    /*
    public BigDecimal calcularValorParcela() {
        return valorPrestador.divide(BigDecimal.valueOf(numParcelas), 2, BigDecimal.ROUND_HALF_UP);
    }
    */

    /**
     * Retorna uma representação textual da tarefa.
     */
    @Override
    public String toString() {
        return "Tarefa{" +
               "ID=" + idTarefa +
               ", ID Lar=" + (idLar != null ? idLar : "Nenhum") +
               ", ID Prestador=" + idPrestador +
               ", Data Início=" + dataInicio.format(FORMATTER) +
               ", Prazo=" + prazoEntrega + " dias" +
               ", Data Entrega=" + getDataEntrega().format(FORMATTER) +
               ", Valor Prestador=" + valorPrestador +
               ", Parcelas=" + numParcelas +
               ", Valor Parcela=" + "IMPLEMENTAR" +
               ", Compra=" + (compra != null ? compra.toString() : "Nenhuma") +
               "}";
    }
}
