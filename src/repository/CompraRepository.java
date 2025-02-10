package repository;

import model.Compra;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class CompraRepository {

    private final Map<String, Compra> compras;

    public CompraRepository() {
        this.compras = new HashMap<>();
    }

    public void adicionar(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("A compra não pode ser nula.");
        }
        if (compras.containsKey(compra.getIdCompra())) {
            throw new IllegalArgumentException("Já existe uma compra com este ID no repositório.");
        }
        this.compras.put(compra.getIdCompra(), compra);
    }

    public void remover(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("A compra não pode ser nula.");
        }
        if (!compras.containsKey(compra.getIdCompra())) {
            throw new IllegalArgumentException("A compra não existe no repositório.");
        }
        this.compras.remove(compra.getIdCompra());
    }

    public Collection<Compra> listar() {
        return this.compras.values();
    }

    public Compra buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.compras.get(id);
    }
}