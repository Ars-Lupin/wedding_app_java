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
    private final String idCasamento;
    private Endereco endereco;
    private BigDecimal valorFesta;
    private LocalDate data;
    private String hora;
    private List<String> convidados;

    /**
     * Construtor da classe Festa.
     * 
     * @param idFesta     Identificador único da festa (32 dígitos numéricos).
     * @param idCasamento Identificador do casamento ao qual a festa pertence.
     * @param endereco    Endereço da festa.
     * @param valorFesta  Valor total gasto na organização da festa.
     * @param data        Data da festa (dd/mm/aaaa).
     * @param hora        Horário que a festa começa (hh:mm).
     * @param convidados  Lista de nomes dos convidados.
     */
    public Festa(String idFesta, String idCasamento, Endereco endereco, double valorFesta, 
                LocalDate data, String hora, List<String> convidados) {
        
        if (idFesta == null || !idFesta.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID da festa deve conter exatamente 32 dígitos numéricos.");
        }
        if (idCasamento == null || !idCasamento.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID do casamento deve conter exatamente 32 dígitos numéricos.");
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
        this.idCasamento = idCasamento;
        this.endereco = endereco;
        this.valorFesta = validarValor(valorFesta, "Valor da festa"); 
        this.data = data;
        this.hora = hora;
        this.convidados = new ArrayList<>(convidados); // Cria uma cópia da lista
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

    public String getIdCasamento() {
        return idCasamento;
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
        return String.format(
                "Festa{ID='%s', CasamentoID='%s', Convidados=%d, Data='%s', Hora='%s', Endereço=%s, Valor=R$ %.2f}",
                idFesta, idCasamento, convidados.size(), data, hora, endereco, valorFesta);
    }
}
