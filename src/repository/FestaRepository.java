package repository;

import model.Festa;

import util.CSVReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collection;

import java.io.IOException;

/**
 * Classe que representa um repositório de festas
 */
public class FestaRepository {

    private final Map<String, Festa> festas;

    /**
     * Construtor: inicializa o mapa de festas
     */
    public FestaRepository() {
        this.festas = new HashMap<>();
    }

    /**
     * Adciona uma festa ao repositório
     * 
     * @param festa
     * @throws IllegalArgumentException Se a festa for nula ou já existir no repositório
     */
    public void adicionar(Festa festa) {
        if (festa == null) {
            throw new IllegalArgumentException("A festa não pode ser nula.");
        }
        if (festas.containsKey(festa.getIdFesta())) {
            throw new IllegalArgumentException("Já existe uma festa com este ID no repositório.");
        }
        this.festas.put(festa.getIdFesta(), festa);
    }

    /**
     * Remove uma festa do repositório
     * 
     * @param festa
     * @throws IllegalArgumentException Se a festa for nula ou não existir no repositório
     */
    public void remover(Festa festa) {
        if (festa == null) {
            throw new IllegalArgumentException("A festa não pode ser nula.");
        }
        if (!festas.containsKey(festa.getIdFesta())) {
            throw new IllegalArgumentException("A festa não existe no repositório.");
        }
        this.festas.remove(festa.getIdFesta());
    }

    /**
     * Lista todas as festas do repositório
     * 
     * @return Uma coleção com todas as festas do repositório
     */
    public Collection<Festa> listar() {
        return this.festas.values();
    }

    /**
     * Busca uma festa pelo ID
     * 
     * @param id
     * @return A festa com o ID especificado
     * @throws IllegalArgumentException Se o ID for nulo ou vazio 
     */
    public Festa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.festas.get(id);
    }

    /**
     * Carrega os dados do arquivo festas.CSV e adiciona as festas ao repositório
     * 
     * @param caminhoArquivo
     * @throws IOException Se ocorrer um erro de leitura do arquivo
     */
    public void carregarDados(String caminhoArquivo) throws IOException {
        List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);
        System.out.println("Arquivo lido com sucesso! Total de linhas: " + linhas.size());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (String[] campos : linhas) {
            
            // Informações básicas da festa
            String idFesta = campos[0].trim();
            String idCasamento = campos[1].trim();
            String local = campos[2].trim();
            LocalDate data = LocalDate.parse(campos[3], formatter);
            String hora = campos[4].trim();
            double valorFesta = Double.parseDouble(campos[5].trim());
            int numConvidados = Integer.parseInt(campos[6].trim());
            
            // Lista de convidados
            List<String> convidados = null;
            if (numConvidados > 0) { // Verifica se há convidados
                convidados = new ArrayList<>();
                for (int i = 7; i < 7 + numConvidados; i++) {
                    // Adiciona o nome do convidado à lista
                    convidados.add(campos[i].trim());
                }
            } else { // Não há convidados
                System.out.println("A festa " + idFesta + " não possui convidados.");
            }

            // Cria a festa e a adiciona ao repositório
            Festa festa = new Festa(idFesta, idCasamento, local, valorFesta, data, hora, convidados);
            this.adicionar(festa);
        }
    }
}
