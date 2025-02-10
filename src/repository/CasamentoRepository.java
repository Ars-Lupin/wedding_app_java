package repository;

import model.Casamento;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class CasamentoRepository {

    private final Map<String, Casamento> casamentos;

    public CasamentoRepository() {
        this.casamentos = new HashMap<>();
    }

    public void adicionar(Casamento casamento) {
        if (casamento == null) {
            throw new IllegalArgumentException("O casamento não pode ser nulo.");
        }
        if (casamentos.containsKey(casamento.getIdCasamento())) {
            throw new IllegalArgumentException("Já existe um casamento com este ID no repositório.");
        }
        this.casamentos.put(casamento.getIdCasamento(), casamento);
    }

    public void remover(Casamento casamento) {
        if (casamento == null) {
            throw new IllegalArgumentException("O casamento não pode ser nulo.");
        }
        if (!casamentos.containsKey(casamento.getIdCasamento())) {
            throw new IllegalArgumentException("O casamento não existe no repositório.");
        }
        this.casamentos.remove(casamento.getIdCasamento());
    }

    public Collection<Casamento> listar() {
        return this.casamentos.values();
    }

    public Casamento buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.casamentos.get(id);
    }
}