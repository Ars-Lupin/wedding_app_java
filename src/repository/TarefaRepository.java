package repository;

import model.Tarefa;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class TarefaRepository {

    private final Map<String, Tarefa> tarefas;

    public TarefaRepository() {
        this.tarefas = new HashMap<>();
    }

    public void adicionar(Tarefa tarefa) {
        if (tarefa == null) {
            throw new IllegalArgumentException("A tarefa não pode ser nula.");
        }
        if (tarefas.containsKey(tarefa.getIdTarefa())) {
            throw new IllegalArgumentException("Já existe uma tarefa com este ID no repositório.");
        }
        this.tarefas.put(tarefa.getIdTarefa(), tarefa);
    }

    public void remover(Tarefa tarefa) {
        if (tarefa == null) {
            throw new IllegalArgumentException("A tarefa não pode ser nula.");
        }
        if (!tarefas.containsKey(tarefa.getIdTarefa())) {
            throw new IllegalArgumentException("A tarefa não existe no repositório.");
        }
        this.tarefas.remove(tarefa.getIdTarefa());
    }

    public Collection<Tarefa> listar() {
        return this.tarefas.values();
    }

    public Tarefa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.tarefas.get(id);
    }
}