package repository;

import model.Tarefa;

import util.CSVReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.text.NumberFormat;
import java.text.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Collection;

import java.io.IOException;


/**
 * Classe que representa um repositório de Tarefas
 */
public class TarefaRepository {

    private final Map<String, Tarefa> tarefas;

    /**
     * Construtor: inicializa a hash map de Tarefas
     */
    public TarefaRepository() {
        this.tarefas = new HashMap<>();
    }

    /**
     * Adiciona uma tarefa ao repositório
     * 
     * @param tarefa
     * @throws IllegalArgumentException Se a tarefa for nulo ou já existir 
     */
    public void adicionar(Tarefa tarefa) {
        if (tarefa == null) {
            throw new IllegalArgumentException("A tarefa não pode ser nula.");
        }
        if (tarefas.containsKey(tarefa.getIdTarefa())) {
            throw new IllegalArgumentException("Já existe uma tarefa com este ID no repositório.");
        }
        this.tarefas.put(tarefa.getIdTarefa(), tarefa);
    }

    /**
     * Remove uma tarefa do repositório
     * 
     * @param tarefa
     * @throws IllegalArgumentException Se a tarefa for nulo ou não existir
     */
    public void remover(Tarefa tarefa) {
        if (tarefa == null) {
            throw new IllegalArgumentException("A tarefa não pode ser nula.");
        }
        if (!tarefas.containsKey(tarefa.getIdTarefa())) {
            throw new IllegalArgumentException("A tarefa não existe no repositório.");
        }
        this.tarefas.remove(tarefa.getIdTarefa());
    }

    /**
     * Lista todas as tarefas do repositório
     * 
     * @return
     */
    public Collection<Tarefa> listar() {
        return this.tarefas.values();
    }

    /**
     * Busca uma tarefa pelo ID
     * 
     * @param caminhoArquivo
     * @throws IOException
     */
    public Tarefa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.tarefas.get(id);
    }

    /**
     * Carrega os dados do arquivo tarefas.CSV e adiciona as tarefas ao repositório
     * 
     * @param caminhoArquivo
     * @throws IOException Se houver um erro de leitura do arquivo
     */
    public void carregarDados(String caminhoArquivo) throws IOException, ParseException {
        List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));

        for (String[] campos : linhas) {
            if (campos.length < 6) { // Verifica se há campos suficientes
                System.err.println("Linha inválida encontrada, ignorando: " + String.join(";", campos));
                continue;
            }

            String idTarefa = campos[0].trim();
            String idLar = campos[1].trim();
            String idPrestador = campos[2].trim();
            LocalDate dataInicio = LocalDate.parse(campos[3].trim(), formatter);
            int prazoEntrega = Integer.parseInt(campos[4].trim());
            double valorPrestador = numberFormat.parse(campos[5].trim()).doubleValue();
            int numParcelas = Integer.parseInt(campos[6].trim());

            // Cria e adiciona a nova tarefa ao repositório
            Tarefa tarefa = new Tarefa(idTarefa, idLar, idPrestador, dataInicio, prazoEntrega, valorPrestador, numParcelas, null);
            this.adicionar(tarefa);            
        }
    }
}
