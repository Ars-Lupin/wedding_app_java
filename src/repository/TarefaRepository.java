package repository;

import model.Tarefa;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Classe que representa um repositório de tarefas.
 */
public class TarefaRepository {

    private Map<String, Tarefa> tarefas;

    // Construtor: inicializa o mapa de tarefas
    public TarefaRepository() {
        this.tarefas = new HashMap<>();
    }

    public void adicionar(Tarefa tarefa) {
        this.tarefas.put(tarefa.getIdTarefa(), tarefa);
    }

    public void remover(Tarefa tarefa) {
        this.tarefas.remove(tarefa.getIdTarefa());
    }

    public Collection<Tarefa> listar() {
        return this.tarefas.values();
    }

    public Tarefa buscarPorId(String id) {
        return this.tarefas.get(id);
    }
}
