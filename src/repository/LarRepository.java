package repository;

import model.Lar;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Classe que representa um repositório de lares.
 */
public class LarRepository {

    private Map<String, Lar> lares;

    // Construtor: inicializa o mapa de lares
    public LarRepository() {
        this.lares = new HashMap<>();
    }

    public void adicionar(Lar lar) {
        this.lares.put(lar.getIdLar(), lar);
    }

    public void remover(Lar lar) {
        this.lares.remove(lar.getIdLar());
    }

    public Collection<Lar> listar() {
        return this.lares.values();
    }

    public Lar buscarPorId(String id) {
        return this.lares.get(id);
    }
}
