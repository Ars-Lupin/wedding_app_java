package model;

/**
 * Classe que representa uma pessoa genérica no sistema.
 */
public class Pessoa {

    private String nome;
    private String telefone;
    private Endereco endereco;

    /**
     * Construtor da classe Pessoa.
     * 
     * @param nome     Nome da pessoa (não pode ser vazio).
     * @param telefone Telefone da pessoa (não pode ser vazio).
     */
    public Pessoa(String nome, String telefone) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone não pode ser vazio.");
        }
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone não pode ser vazio.");
        }
        this.telefone = telefone;
    }

    /**
     * Retorna uma representação textual do objeto. (Para testes de sanidade, por exemplo)
     * 
     * @return String formatada com os dados da pessoa.
     */
    @Override
    public String toString() {
        return "Pessoa{" +
               "nome='" + nome + '\'' +
               ", telefone='" + telefone + '\'' +
               '}';
    }
}
