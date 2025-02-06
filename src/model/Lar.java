package model;

/**
 * Classe que representa um lar no sistema.
 */
public class Lar {

    private int idLar;
    private Casal casal;
    private Endereco endereco;

    /**
     * Construtor da classe Lar.
     * 
     * @param idLar    Identificador único do lar.
     * @param casal    Casal proprietário do lar.
     * @param endereco Endereço do lar.
     */
    public Lar(int idLar, Casal casal, Endereco endereco) {
        if (idLar <= 0) {
            throw new IllegalArgumentException("O ID do lar deve ser um número positivo.");
        }
        if (casal == null) {
            throw new IllegalArgumentException("O casal não pode ser nulo.");
        }
        if (endereco == null) {
            throw new IllegalArgumentException("O endereço do lar não pode ser nulo.");
        }

        this.idLar = idLar;
        this.casal = casal;
        this.endereco = endereco;
    }

    public int getIdLar() {
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
        return "Lar{" +
               "ID=" + idLar +
               ", casal=" + casal +
               ", endereco=" + endereco +
               '}';
    }
}
