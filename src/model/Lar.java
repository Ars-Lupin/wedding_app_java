package model;

/**
 * Classe que representa um lar no sistema.
 */
public class Lar {

    private final String idLar;
    private final Casal casal;
    private Endereco endereco;

    /**
     * Construtor da classe Lar.
     * 
     * @param idLar      Identificador único do lar (32 dígitos numéricos).
     * @param casal      Casal que mora no lar.
     * @param endereco   Endereço do lar.
     */
    public Lar(String idLar, Casal casal, Endereco endereco) {
        if (idLar == null || !idLar.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID do lar deve conter exatamente 32 dígitos numéricos.");
        }
        if (endereco == null) {
            throw new IllegalArgumentException("O endereço do lar não pode ser nulo.");
        }

        this.idLar = idLar;
        this.casal = casal;
        this.endereco = endereco;
    }

    public String getIdLar() {
        return idLar;
    }

    public Casal getCasal() {
        return casal;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("O endereço do lar não pode ser nulo.");
        }
        this.endereco = endereco;
    }

    /**
     * Retorna uma representação textual do lar.
     * 
     * @return String formatada com os dados do lar.
     */
    @Override
    public String toString() {
        return String.format("Lar{ID='%s', Endereço=%s}",
                idLar, casal, endereco);
    }
}
