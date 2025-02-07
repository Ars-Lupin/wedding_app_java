package model;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe que representa uma festa no sistema.
 */
public class Festa {
    
    private final String idFesta;
    private final Casamento casamento;
    private Endereco endereco;
    private BigDecimal valorFesta;
    private LocalDate data;
    private String hora;
    private List<String> convidados;

    /**
     * Construtor da classe Festa
     * @param idFesta identificador único da festa
     * @param casamento casamento que será celebrado
     * @param endereco endereço da festa
     * @param valorFesta valor total gasto na organização da festa
     * @param data data da festa (dd/mm/aaaa)
     * @param hora horário que a festa começa (hh:mm)
     * @param convidados lista de nomes dos convidados
     */
    public Festa(String idFesta, Casamento casamento, Endereco endereco, double valorFesta, 
                LocalDate data, String hora, List<String> convidados) {
        
        if (idFesta == null || !idFesta.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID da festa deve ter exatamente 32 dígitos numéricos.");
        }
        if (casamento == null) {
            throw new IllegalArgumentException("O casamento não pode ser nulo.");
        }
        if (endereco == null) {
            throw new IllegalArgumentException("O endereço não pode ser nulo.");
        }
        if (data == null) {
            throw new IllegalArgumentException("A data da festa não pode ser nula.");
        }
        if (hora == null || !hora.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            throw new IllegalArgumentException("A hora da festa deve estar no formato HH:mm (00:00 a 23:59).");
        }
        if (convidados == null) {
            throw new IllegalArgumentException("A lista de convidados não pode ser nula.");
        }

        this.idFesta = idFesta;
        this.casamento = casamento;
        this.endereco = endereco;
        this.valorFesta = validarValor(valorFesta, "Valor da festa"); 
        this.data = data;
        this.hora = hora;
        this.convidados = new ArrayList<>(convidados); // Evita modificações externas
    }

    private BigDecimal validarValor(double valor, String campo) {
        if (valor < 0) {
            throw new IllegalArgumentException(campo + " não pode ser negativo.");
        }
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    public String getIdFesta() {
        return idFesta;
    }

    public Casamento getCasamento() {
        return casamento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("O endereço não pode ser nulo.");
        }
        this.endereco = endereco;
    }

    public BigDecimal getValorFesta() {
        return valorFesta;
    }

    public void setValorFesta(double valorFesta) {
        this.valorFesta = validarValor(valorFesta, "Valor da festa"); 
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("A data da festa não pode ser nula.");
        }
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        if (hora == null || !hora.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            throw new IllegalArgumentException("A hora da festa deve estar no formato HH:mm (00:00 a 23:59).");
        }
        this.hora = hora;
    }

    public List<String> getConvidados() {
        return Collections.unmodifiableList(convidados); // Retorna uma cópia imutável
    }

    public void adicionarConvidado(String convidado) {
        if (convidado == null || convidado.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do convidado não pode ser vazio.");
        }
        convidados.add(convidado);
    }

    public void removerConvidado(String convidado) {
        if (convidado == null || convidado.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do convidado não pode ser vazio.");
        }
        convidados.remove(convidado);
    }

    @Override
    public String toString() {
        return "Festa{" +
               "ID=" + idFesta +
               ", casamentoID=" + casamento.getIdCasamento() + // Exibe apenas o ID para evitar loops
               ", convidados=" + convidados.size() + // Exibe número total de convidados
               ", data=" + data +
               ", endereco=" + endereco +
               ", hora=" + hora +
               ", valorFesta=" + valorFesta +
               "}";
    }
}
