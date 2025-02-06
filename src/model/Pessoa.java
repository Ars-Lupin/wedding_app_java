package model;

/**
 * Classe que representa uma pessoa genérica no sistema.
 */
public class Pessoa {

    private String nome;
    private String telefone;
    private Endereco endereco;
    private String idPessoa;

    /**
     * Construtor da classe Pessoa.
     * 
     * @param nome     Nome da pessoa (não pode ser vazio).
     * @param telefone Telefone da pessoa (não pode ser vazio).
     * @param endereco Endereço da pessoa (não pode ser vazio).
     * @param idPessoa Identificador único da pessoa.
     */
    public Pessoa(String nome, String telefone, Endereco endereco, String idPessoa) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone não pode ser vazio.");
        }
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser vazio.");
        }
        if (idPessoa == null || !idPessoa.matches("\\d{32}")) {
            throw new IllegalArgumentException("ID inválido! Deve conter exatamente 32 dígitos numéricos.");
        }
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.idPessoa = idPessoa;
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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser vazio.");
        }
        this.endereco = endereco;
    }

    public String getIdPessoa() {
        return idPessoa;
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
                ", endereco=" + endereco +
                ", idPessoa='" + idPessoa + '\'' +
               '}';
    }
}
