package model;

import exception.DataInconsistencyException;

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
    public Pessoa(String nome, String telefone, String endereco, String idPessoa) throws DataInconsistencyException {
        if (idPessoa == null || !idPessoa.matches("\\d{32}")) {
            throw new DataInconsistencyException("ID da Pessoa inválido! Deve conter exatamente 32 dígitos numéricos.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new DataInconsistencyException("Nome da pessoa não pode ser vazio.");
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new DataInconsistencyException("Telefone da pessoa não pode ser vazio.");
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new DataInconsistencyException("Endereço da pessoa não pode ser nulo.");
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

    public void setNome(String nome) throws DataInconsistencyException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DataInconsistencyException("Nome não pode ser vazio.");
        }
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) throws DataInconsistencyException {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new DataInconsistencyException("Telefone não pode ser vazio.");
        }
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) throws DataInconsistencyException {
        if (endereco == null) {
            throw new DataInconsistencyException("Endereço não pode ser nulo.");
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
