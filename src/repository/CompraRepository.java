package repository;

import model.Compra;

import util.CSVReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

import java.io.IOException;

/**
 * Classe que representa um repositório de Compras
 */
public class CompraRepository {

    private final Map<String, Compra> compras;

    /**
     * Construtor: inicializa o mapa de Compras
     */
    public CompraRepository() {
        this.compras = new HashMap<>();
    }

    /**
     * Adiciona uma Compra ao repositório
     * @param compra
     */
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