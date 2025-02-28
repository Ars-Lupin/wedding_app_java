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

        if (idPessoa1 == null || !idPessoa1.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID da primeira pessoa do casal deve conter exatamente 32 dígitos numéricos.");
        }
        if (idPessoa2 == null || !idPessoa2.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID da segunda pessoa do casal deve conter exatamente 32 dígitos numéricos.");
        }
        // Se o casal é casado, o ID do casamento deve ser válido
        if (idCasamento != null && !idCasamento.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID do casamento do casal deve conter exatamente 32 dígitos numéricos.");
        }
        // Se o casal mora junto, o ID do lar deve ser válido
        if (idLar != null && !idLar.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID do lar do casal deve conter exatamente 32 dígitos numéricos.");
        }

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
        return String.format("Casal{Pessoa1 = '%s', Pessoa2 = '%s', Casamento = %s, Lar = %s}",
                idPessoa1, idPessoa2, temCasamento() ? idCasamento : "Nenhum", temLar() ? idLar : "Nenhum");
    }
}
