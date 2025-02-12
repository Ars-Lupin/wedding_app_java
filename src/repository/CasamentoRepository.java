package repository;

import model.Casamento;

import util.CSVReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

import java.io.IOException;

/**
 * Classe que representa um repositório de casamentos
 */
public class CasamentoRepository {

    private final Map<String, Casamento> casamentos;

    /**
     * Construtor: inicializa o mapa de casamentos
     */
    public CasamentoRepository() {
        this.casamentos = new HashMap<>();
    }

    /**
     * Adiciona um casamento ao repositório
     * 
     * @param casamento
     * @throws IllegalArgumentException Se o casamento for nulo ou já existir no repositório
     */
    public void adicionar(Casamento casamento) {
        if (casamento == null) {
            throw new IllegalArgumentException("O casamento não pode ser nulo.");
        }
        if (casamentos.containsKey(casamento.getIdCasamento())) {
            throw new IllegalArgumentException("Já existe um casamento com este ID no repositório.");
        }
        this.casamentos.put(casamento.getIdCasamento(), casamento);
    }

    /**
     * Remove um casamento do repositório
     * 
     * @param casamento
     * @throws IllegalArgumentException Se o lar for nulo ou não existir no repositório
     */
    public void remover(Casamento casamento) {
        if (casamento == null) {
            throw new IllegalArgumentException("O casamento não pode ser nulo.");
        }
        if (!casamentos.containsKey(casamento.getIdCasamento())) {
            throw new IllegalArgumentException("O casamento não existe no repositório.");
        }
        this.casamentos.remove(casamento.getIdCasamento());
    }

    /**
     * Lista todos os casamentos do repositório
     * 
     * @return Uma coleção com todos os casamentos do repositório
     */
    public Collection<Casamento> listar() {
        return this.casamentos.values();
    }

    /**
     * Busca um casamento pelo ID
     * 
     * @param id
     * @return O casamento com o ID especificado
     * @throws IllegalArgumentException Se o ID for nulo ou vazio
     */
    public Casamento buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.casamentos.get(id);
    }

    /**
     * Carrega os dados do arquivo casamentos.CSV e adiciona os casamentos ao repositório
     * 
     * @param caminhoArquivo
     * @throws IOException Se houver um erro de leitura do arquivo
     */
    public void carregarDados(String caminhoArquivo) throws IOException {
        List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);
        System.out.println("Arquivo lido com sucesso! Total de linhas: " + linhas.size());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (String[] campos : linhas) {
            if (campos.length < 6) { // Verifica se há campos suficientes
                System.err.println("Linha inválida encontrada, ignorando: " + String.join(";", campos));
                continue;
            }

            // Informações dos IDs que envolvem um casamento
            String idCasamento = campos[0].trim();
            String idPessoa1 = campos[1].trim();
            String idPessoa2 = campos[2].trim();
            LocalDate data = LocalDate.parse(campos[3].trim(), formatter);
            String hora = campos[4].trim();
            String local = campos[5].trim();

            // Cria e adiciona o novo casamento ao repositório
            Casamento casamento = new Casamento(idCasamento, idPessoa1, idPessoa2, data, hora, local);
            this.adicionar(casamento);
        }
    }
}
