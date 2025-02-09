package repository;

import model.Pessoa;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Classe que representa um repositório de pessoas.
 */
public class PessoaRepository {

    private Map<String, Pessoa> pessoas;

    // Construtor: inicializa o mapa de pessoas
    public PessoaRepository() {
        this.pessoas = new HashMap<>();
    }

    public void adicionar(Pessoa pessoa) {
        this.pessoas.put(pessoa.getIdPessoa(), pessoa);
    }

    public void remover(Pessoa pessoa) {
        this.pessoas.remove(pessoa.getIdPessoa());
    }

    public Collection<Pessoa> listar() {
        return this.pessoas.values();
    }

    public Pessoa buscarPorId(String id) {
        return this.pessoas.get(id);
    }
}
