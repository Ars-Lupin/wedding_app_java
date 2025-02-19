package model;

/**
 * Classe que representa um casal no sistema.
 */
public class Casal {

    private final String idPessoa1;
    private final String idPessoa2;

    /**
     * Construtor da classe Casal.
     *
     * @param idPessoa1 Identificador da primeira pessoa do casal.
     * @param idPessoa2 Identificador da segunda pessoa do casal.
     */
    public Casal(String idPessoa1, String idPessoa2) {
        if (idPessoa1 == null || !idPessoa1.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID da primeira pessoa deve conter exatamente 32 dígitos numéricos.");
        }
        if (idPessoa2 == null || !idPessoa2.matches("\\d{32}")) {
            throw new IllegalArgumentException("O ID da segunda pessoa deve conter exatamente 32 dígitos numéricos.");
        }
        if (idPessoa1.equals(idPessoa2)) {
            throw new IllegalArgumentException("As duas pessoas devem ter IDs diferentes.");
        }

        this.idPessoa1 = idPessoa1;
        this.idPessoa2 = idPessoa2;
    }

    public String getIdPessoa1() {
        return idPessoa1;
    }

    public String getIdPessoa2() {
        return idPessoa2;
    }

    /**
     * Retorna uma representação textual do casal.
     * 
     * @return String formatada com os dados do casal.
     */
    @Override
    public String toString() {
        return String.format("Casal{Pessoa1='%s', Pessoa2='%s'}", idPessoa1, idPessoa2);
    }
}
