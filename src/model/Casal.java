package model;

/**
 * Classe que representa um casal.
 * Um casal pode ter um casamento e/ou um lar associado.
 */
public class Casal {
    private String idPessoa1;
    private String idPessoa2;
    private String idCasamento;
    private String idLar;

    /**
     * Construtor da classe Casal.
     * 
     * @param idPessoa1   ID da primeira pessoa do casal.
     * @param idPessoa2   ID da segunda pessoa do casal.
     * @param idCasamento ID do casamento (pode ser null se não forem casados).
     * @param idLar       ID do lar (pode ser null se não morarem juntos).
     */
    public Casal(String idPessoa1, String idPessoa2, String idCasamento, String idLar) {
        this.idPessoa1 = idPessoa1;
        this.idPessoa2 = idPessoa2;
        this.idCasamento = idCasamento;
        this.idLar = idLar;
    }

    // Getters
    public String getIdPessoa1() {
        return idPessoa1;
    }

    public String getIdPessoa2() {
        return idPessoa2;
    }

    public String getIdCasamento() {
        return idCasamento;
    }

    public String getIdLar() {
        return idLar;
    }

    // Setters
    public void setIdCasamento(String idCasamento) {
        this.idCasamento = idCasamento;
    }

    public void setIdLar(String idLar) {
        this.idLar = idLar;
    }

    /**
     * Verifica se o casal possui um casamento registrado.
     * 
     * @return true se o casal tem um casamento, false caso contrário.
     */
    public boolean temCasamento() {
        return idCasamento != null && !idCasamento.isEmpty();
    }

    /**
     * Verifica se o casal possui um lar registrado.
     * 
     * @return true se o casal tem um lar, false caso contrário.
     */
    public boolean temLar() {
        return idLar != null && !idLar.isEmpty();
    }

    /**
     * Retorna uma representação textual do casal.
     */
    @Override
    public String toString() {
        return "Casal {" +
                "Pessoa 1 = '" + idPessoa1 + '\'' +
                ", Pessoa 2 = '" + idPessoa2 + '\'' +
                ", Casamento = " + (temCasamento() ? idCasamento : "Nenhum") +
                ", Lar = " + (temLar() ? idLar : "Nenhum") +
                '}';
    }
}
