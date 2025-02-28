package model;

import java.time.LocalDate;

/**
 * Classe que representa um casamento no sistema.
 */
public class Casamento {

    private final String idCasamento;
    private final Casal casal;
    private LocalDate dataCasamento;
    private String horaCasamento;
    private String localCerimonia;
    private Festa festa; // Pode ser null

    /**
     * Construtor para casamento sem festa.
     *
     * @param idCasamento    Identificador único do casamento (32 dígitos numéricos).
     * @param casal          Casal que está se casando.    
     * @param dataCasamento  Data do casamento.
     * @param horaCasamento  Hora do casamento (hh:mm).
     * @param localCerimonia Local da cerimônia.
     */
    public Casamento(String idCasamento, Casal casal, LocalDate dataCasamento, 
                        String horaCasamento, String localCerimonia) {
        this(idCasamento, casal, dataCasamento, horaCasamento, localCerimonia, null);
    }

    /**
     * Construtor para casamento com ou sem festa.
     *
     * @param idCasamento    Identificador único do casamento (32 dígitos numéricos).
     * @param casal          Casal que está se casando.
     * @param dataCasamento  Data do casamento.
     * @param horaCasamento  Hora do casamento (hh:mm).
     * @param localCerimonia Local da cerimônia.
     * @param festa          Festa do casamento (pode ser null).
     */
    public Casamento(String idCasamento, Casal casal, LocalDate dataCasamento, 
                        String horaCasamento, String localCerimonia, Festa festa) {
        if (idCasamento == null || !idCasamento.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID do casamento deve conter exatamente 32 dígitos numéricos.");
        }
        if (dataCasamento == null) {
            throw new IllegalArgumentException("A data do casamento não pode ser nula.");
        }
        if (horaCasamento == null || !horaCasamento.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            throw new IllegalArgumentException("A hora do casamento deve estar no formato HH:mm (00:00 a 23:59).");
        }
        if (localCerimonia == null || localCerimonia.trim().isEmpty()) {
            throw new IllegalArgumentException("O local da cerimônia não pode ser vazio.");
        }

        this.idCasamento = idCasamento;
        this.casal = casal;
        this.dataCasamento = dataCasamento;
        this.horaCasamento = horaCasamento;
        this.localCerimonia = localCerimonia;
        this.festa = festa; // Pode ser null
    }

    public String getIdCasamento() {
        return idCasamento;
    }

    public Casal getCasal() {
        return casal;
    }

    public LocalDate getDataCasamento() {
        return dataCasamento;
    }

    public void setDataCasamento(LocalDate dataCasamento) {
        if (dataCasamento == null) {
            throw new IllegalArgumentException("A data do casamento não pode ser nula.");
        }
        this.dataCasamento = dataCasamento;
    }

    public String getHoraCasamento() {
        return horaCasamento;
    }

    public void setHoraCasamento(String horaCasamento) {
        if (horaCasamento == null || !horaCasamento.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            throw new IllegalArgumentException("A hora do casamento deve estar no formato HH:mm (00:00 a 23:59).");
        }
        this.horaCasamento = horaCasamento;
    }

    public String getLocalCerimonia() {
        return localCerimonia;
    }

    public void setLocalCerimonia(String localCerimonia) {
        if (localCerimonia == null || localCerimonia.trim().isEmpty()) {
            throw new IllegalArgumentException("O local da cerimônia não pode ser vazio.");
        }
        this.localCerimonia = localCerimonia;
    }

    public Festa getFesta() {
        return festa;
    }

    public void setFesta(Festa festa) {
        this.festa = festa;
    }

    /**
     * Retorna uma representação textual do casamento.
     */
    @Override
    public String toString() {
        return String.format(
                "Casamento{ID='%s', Data='%s', Hora='%s', Local='%s', Festa=%s}",
                idCasamento, casal, dataCasamento, horaCasamento, localCerimonia,
                (festa != null ? festa : "Sem festa"));
    }
}
