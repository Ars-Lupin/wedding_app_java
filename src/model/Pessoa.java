package model;

/**
 * Classe que representa uma pessoa genérica no sistema.
 */
public class Pessoa {

    private final String idPessoa;
    private String nome;
    private String telefone;
    private String endereco;

    /**
     * Construtor da classe Pessoa.
     * 
     * @param nome     Nome da pessoa (não pode ser vazio).
     * @param telefone Telefone da pessoa (não pode ser vazio).
     * @param endereco Endereço da pessoa (não pode ser nulo).
     * @param idPessoa Identificador único da pessoa (32 dígitos).
     */
    public Pessoa(String nome, String telefone, String endereco, String idPessoa) {
        if (idPessoa == null || !idPessoa.matches("\\d{32}")) {
            throw new IllegalArgumentException("ID inválido! Deve conter exatamente 32 dígitos numéricos.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone não pode ser vazio.");
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço não pode ser nulo.");
        }

        this.idPessoa = idPessoa;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public String getIdPessoa() {
        return idPessoa;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo.");
        }
        this.endereco = endereco;
    }

    /**
     * Retorna uma representação textual da pessoa.
     */
    @Override
    public String toString() {
        return String.format("Pessoa{ID='%s', Nome='%s', Telefone='%s', Endereço=%s}", 
                             idPessoa, nome, telefone, endereco);
    }
}
