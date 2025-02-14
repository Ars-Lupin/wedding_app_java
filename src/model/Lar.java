package model;

/**
 * Classe que representa um lar no sistema.
 */
public class Lar {

    private final String idLar;
    private final String idPessoa1;
    private final String idPessoa2;
    private Endereco endereco;

    /**
     * Construtor da classe Lar.
     * 
     * @param idLar      Identificador único do lar (32 dígitos numéricos).
     * @param idPessoa1  Identificador da primeira pessoa do casal.
     * @param idPessoa2  Identificador da segunda pessoa do casal.
     * @param endereco   Endereço do lar.
     */
    public Lar(String idLar, String idPessoa1, String idPessoa2, Endereco endereco) {
        if (idLar == null || !idLar.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID do lar deve conter exatamente 32 dígitos numéricos.");
        }
        if (idPessoa1 == null || !idPessoa1.matches("\\d{37}")) {
            throw new IllegalArgumentException("O ID da primeira pessoa deve conter exatamente 37 dígitos numéricos.");
        }
        if (idPessoa2 == null || !idPessoa2.matches("\\d{37}")) {
            throw new IllegalArgumentException("O ID da segunda pessoa deve conter exatamente 37 dígitos numéricos.");
        }
        if (idPessoa1.equals(idPessoa2)) {
            throw new IllegalArgumentException("As duas pessoas devem ter IDs diferentes.");
        }
        if (endereco == null) {
            throw new IllegalArgumentException("O endereço do lar não pode ser nulo.");
        }

        this.idLar = idLar;
        this.idPessoa1 = idPessoa1;
        this.idPessoa2 = idPessoa2;
        this.endereco = endereco;
    }

    public String getIdLar() {
        return idLar;
    }

    public String getIdPessoa1() {
        return idPessoa1;
    }

    public String getIdPessoa2() {
        return idPessoa2;
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
        return String.format("Lar{ID='%s', Pessoa1='%s', Pessoa2='%s', Endereço=%s}",
                idLar, idPessoa1, idPessoa2, endereco);
    }
}
