package model;

/**
 * Classe que representa um endereco
 */
public class Endereco {
    
    private String rua;
    private String numero;
    private String complemento;

    /**
     * Construtor da classe Endereco
     * 
     * @param rua           Nome da rua (não pode ser vazio).
     * @param numero        Número da casa (não pode ser vazio).
     * @param complemento   complemento do endereço (não pode ser vazio).
     */
    public Endereco (String rua, String numero, String complemento) {
        if (rua == null || rua.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da rua não pode ser vazio.");
        }
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("numero da casa não pode ser vazio."); }
        if (complemento == null || complemento.trim().isEmpty()) {
            throw new IllegalArgumentException("complemento não pode ser vazio.");
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

    public String getnumero() {
        return numero;
    }

    public void setnumero(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("numero da casa não pode ser vazio.");
        }
        this.numero = numero;
    }

    public String getcomplemento() {
        return complemento;
    }

    public void setcomplemento(String complemento) {
        if (complemento == null || complemento.trim().isEmpty()) {
            throw new IllegalArgumentException("complemento não pode ser vazio.");
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
        return "Endereco{" +
               "rua='" + rua + '\'' +
               ", numero='" + numero + '\'' +
               ", complemento='" + complemento + '\'' + 
               '}';
    }
}
