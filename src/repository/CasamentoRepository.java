package repository;

import model.Casamento;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Classe que representa um repositório de casamentos.
 */
public class CasamentoRepository {

    private Map<String, Casamento> casamentos;

    // Construtor: inicializa o mapa de casamentos
    public CasamentoRepository() {
        this.casamentos = new HashMap<>();
    }

    public void adicionar(Casamento casamento) {
        this.casamentos.put(casamento.getIdCasamento(), casamento);
    }

    public void remover(Casamento casamento) {
        this.casamentos.remove(casamento.getIdCasamento());
    }

    public Collection<Casamento> listar() {
        return this.casamentos.values();
    }

    public Casamento buscarPorId(String id) {
        return this.casamentos.get(id);
    }
}