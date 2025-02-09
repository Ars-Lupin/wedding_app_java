package repository;

import model.Compra;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Classe que representa um repositório de compras.
 */
public class CompraRepository {

    private Map<String, Compra> compras;

    // Construtor: inicializa o mapa de compras
    public CompraRepository() {
        this.compras = new HashMap<>();
    }

    public void adicionar(Compra compra) {
        this.compras.put(compra.getIdCompra(), compra);
    }

    public void remover(Compra compra) {
        this.compras.remove(compra.getIdCompra());
    }

    public Collection<Compra> listar() {
        return this.compras.values();
    }

    public Compra buscarPorId(String id) {
        return this.compras.get(id);
    }
}
