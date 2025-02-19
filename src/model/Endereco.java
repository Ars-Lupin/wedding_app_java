package model;

/**
 * Classe que representa um endereco
 */
public class Endereco {
    
    private String rua;
    private int numero;
    private String complemento;

    /**
     * Construtor da classe Endereco
     * 
     * @param rua           Nome da rua (não pode ser vazio).
     * @param numero        Número da casa (não pode ser vazio).
     * @param complemento   complemento do endereço (opcional).
     */
    public Endereco (String rua, int numero, String complemento) {
        if (rua == null || rua.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da rua não pode ser vazio.");
        }

        if (numero <= 0) {
            throw new IllegalArgumentException("Número da casa não pode ser menor ou igual a zero");
        }

        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        if (rua == null || rua.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da rua não pode ser vazio.");
        }
        this.rua = rua;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("Número da casa não pode ser menor ou igual a zero");
        }
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        if (complemento == null || complemento.trim().isEmpty()) {
            throw new IllegalArgumentException("Complemento não pode ser vazio.");
        }
        this.complemento = complemento;
    }

    /**
     * Retorna uma representação textual do objeto. (Para testes de sanidade, por exemplo)
     * 
     * @return String formatada com os dados do endereço.
     */
    @Override
    public String toString() {
        return String.format("%s, %d%s", rua, numero, complemento == null ? "" : ", " + complemento);}
}
