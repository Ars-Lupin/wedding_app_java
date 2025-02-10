package repository;

import model.Lar;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class LarRepository {

    private final Map<String, Lar> lares;

    public LarRepository() {
        this.lares = new HashMap<>();
    }

    public void adicionar(Lar lar) {
        if (lar == null) {
            throw new IllegalArgumentException("O lar não pode ser nulo.");
        }
        if (lares.containsKey(lar.getIdLar())) {
            throw new IllegalArgumentException("Já existe um lar com este ID no repositório.");
        }
        this.lares.put(lar.getIdLar(), lar);
    }

    public void remover(Lar lar) {
        if (lar == null) {
            throw new IllegalArgumentException("O lar não pode ser nulo.");
        }
        if (!lares.containsKey(lar.getIdLar())) {
            throw new IllegalArgumentException("O lar não existe no repositório.");
        }
        this.lares.remove(lar.getIdLar());
    }

    public Collection<Lar> listar() {
        return this.lares.values();
    }

    public Lar buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.lares.get(id);
    }
}