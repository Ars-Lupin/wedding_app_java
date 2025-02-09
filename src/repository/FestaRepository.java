package repository;

import model.Festa;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Classe que representa um repositório de festas.
 */
public class FestaRepository {

    private Map<String, Festa> festas;

    // Construtor: inicializa o mapa de festas
    public FestaRepository() {
        this.festas = new HashMap<>();
    }

    public void adicionar(Festa festa) {
        this.festas.put(festa.getIdFesta(), festa);
    }

    public void remover(Festa festa) {
        this.festas.remove(festa.getIdFesta());
    }

    public Collection<Festa> listar() {
        return this.festas.values();
    }

    public Festa buscarPorId(String id) {
        return this.festas.get(id);
    }
}
