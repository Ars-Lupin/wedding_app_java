package repository;

import model.Festa;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class FestaRepository {

    private final Map<String, Festa> festas;

    public FestaRepository() {
        this.festas = new HashMap<>();
    }

    public void adicionar(Festa festa) {
        if (festa == null) {
            throw new IllegalArgumentException("A festa não pode ser nula.");
        }
        if (festas.containsKey(festa.getIdFesta())) {
            throw new IllegalArgumentException("Já existe uma festa com este ID no repositório.");
        }
        this.festas.put(festa.getIdFesta(), festa);
    }

    public void remover(Festa festa) {
        if (festa == null) {
            throw new IllegalArgumentException("A festa não pode ser nula.");
        }
        if (!festas.containsKey(festa.getIdFesta())) {
            throw new IllegalArgumentException("A festa não existe no repositório.");
        }
        this.festas.remove(festa.getIdFesta());
    }

    public Collection<Festa> listar() {
        return this.festas.values();
    }

    public Festa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.festas.get(id);
    }
}