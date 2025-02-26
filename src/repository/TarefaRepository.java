package repository;

import model.Tarefa;
import model.Lar;
import model.Pessoa;
import util.CSVReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
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
     * @param tarefa Tarefa a ser adicionada.
     * @throws IllegalArgumentException Se a tarefa for nula ou já existir.
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
     * @param tarefa Tarefa a ser removida.
     * @throws IllegalArgumentException Se a tarefa for nula ou não existir.
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
     * Lista todas as tarefas do repositório.
     * 
     * @return Coleção de todas as tarefas cadastradas.
     */
    public Collection<Tarefa> listar() {
        return this.tarefas.values();
    }

    /**
     * Busca uma tarefa pelo ID
     * 
     * @param id ID da tarefa.
     * @return A tarefa correspondente ao ID, ou null se não encontrada.
     * @throws IllegalArgumentException Se o ID for nulo ou inválido.
     */
    public Tarefa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.tarefas.get(id);
    }

    /**
     * Carrega os dados do arquivo tarefas.CSV e adiciona as tarefas ao repositório.
     * Agora inclui validação para verificar se os IDs do Lar e do Prestador de Serviço existem.
     *
     * @param caminhoArquivo Caminho do arquivo CSV.
     * @param larRepo Repositório de lares para validar os IDs.
     * @param pessoaRepo Repositório de pessoas para validar os IDs dos prestadores de serviço.
     * @throws IOException Se houver um erro de leitura do arquivo.
     * @throws ParseException Se houver um erro na conversão de valores numéricos.
     */
    public void carregarDados(String caminhoArquivo, LarRepository larRepo, PessoaRepository pessoaRepo) throws IOException, ParseException {
        List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));

        for (String[] campos : linhas) {
            if (campos.length < 7) { // Verifica se há campos suficientes
                System.out.println("Linha inválida encontrada, ignorando: " + String.join(";", campos));
                continue;
            }

            String idTarefa = campos[0].trim();

            // Verifica se o ID já existe no repositório
            if (this.tarefas.containsKey(idTarefa)) {
                throw new IllegalArgumentException("ID repetido " + idTarefa + " na classe Tarefa.");
            }

            String idLar = campos[1].trim();
            String idPrestador = campos[2].trim();
            LocalDate dataInicio = LocalDate.parse(campos[3].trim(), formatter);
            int prazoEntrega = Integer.parseInt(campos[4].trim());
            double valorPrestador = numberFormat.parse(campos[5].trim()).doubleValue();
            int numParcelas = Integer.parseInt(campos[6].trim());

            // 🔹 Validação: Verifica se o ID do Lar existe
            Lar lar = larRepo.buscarPorId(idLar);
            if (lar == null) {
                throw new IllegalArgumentException("ID(s) de Lar " + idLar + " não cadastrado na Tarefa de ID " + idTarefa + ".");
            }

            // 🔹 Validação: Verifica se o ID do Prestador de Serviço existe
            Pessoa prestador = pessoaRepo.buscarPorId(idPrestador);
            if (prestador == null) {
                throw new IllegalArgumentException("ID(s) de Prestador de Serviço " + idPrestador + " não cadastrado na Tarefa de ID " + idTarefa + ".");
            }

            // Cria e adiciona a nova tarefa ao repositório
            Tarefa tarefa = new Tarefa(idTarefa, idLar, idPrestador, dataInicio, prazoEntrega, valorPrestador, numParcelas, null);
            this.adicionar(tarefa);
        }
    }
}
