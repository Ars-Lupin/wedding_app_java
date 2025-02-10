package repository;

import model.Pessoa;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Classe que representa um repositório de pessoas.
 */
public class PessoaRepository {

    private final Map<String, Pessoa> pessoas;

    /**
     * Construtor: inicializa o mapa de pessoas.
     */
    public PessoaRepository() {
        this.pessoas = new HashMap<>();
    }

    /**
     * Adiciona uma pessoa ao repositório.
     * 
     * @param pessoa Pessoa a ser adicionada.
     * @throws IllegalArgumentException Se a pessoa for nula ou já existir no repositório.
     */
    public void adicionar(Pessoa pessoa) {
        if (pessoa == null) {
            throw new IllegalArgumentException("A pessoa não pode ser nula.");
        }
        if (pessoas.containsKey(pessoa.getIdPessoa())) {
            throw new IllegalArgumentException("Já existe uma pessoa com este ID no repositório.");
        }
        this.pessoas.put(pessoa.getIdPessoa(), pessoa);
    }

    /**
     * Remove uma pessoa do repositório.
     * 
     * @param pessoa Pessoa a ser removida.
     * @throws IllegalArgumentException Se a pessoa for nula ou não existir no repositório.
     */
    public void remover(Pessoa pessoa) {
        if (pessoa == null) {
            throw new IllegalArgumentException("A pessoa não pode ser nula.");
        }
        if (!pessoas.containsKey(pessoa.getIdPessoa())) {
            throw new IllegalArgumentException("A pessoa não existe no repositório.");
        }
        this.pessoas.remove(pessoa.getIdPessoa());
    }

    /**
     * Retorna uma coleção contendo todas as pessoas cadastradas.
     * 
     * @return Coleção de pessoas no repositório.
     */
    public Collection<Pessoa> listar() {
        return this.pessoas.values();
    }

    /**
     * Busca uma pessoa pelo ID.
     * 
     * @param id ID da pessoa.
     * @return A pessoa correspondente ao ID, ou null se não for encontrada.
     * @throws IllegalArgumentException Se o ID for nulo ou inválido.
     */
    public Pessoa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.pessoas.get(id);
    }
}
