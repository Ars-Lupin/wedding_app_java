package repository;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import model.Casamento;
import model.Festa;
import util.CSVReader;

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
     * Adiciona uma festa ao repositório
     * 
     * @param festa Festa a ser adicionada.
     * @throws IllegalArgumentException Se a festa for nula ou já existir.
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
     * @param festa Festa a ser removida.
     * @throws IllegalArgumentException Se a festa for nula ou não existir.
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
     * @return Uma coleção com todas as festas do repositório.
     */
    public Collection<Festa> listar() {
        return this.festas.values();
    }

    /**
     * Busca uma festa pelo ID
     * 
     * @param id ID da festa.
     * @return A festa com o ID especificado.
     * @throws IllegalArgumentException Se o ID for nulo ou vazio.
     */
    public Festa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.festas.get(id);
    }

    /**
     * Carrega os dados do arquivo festas.CSV e adiciona as festas ao repositório
     * Agora inclui validação para verificar se os IDs do casamento existem.
     *
     * @param caminhoArquivo Caminho do arquivo CSV.
     * @param casamentoRepo Repositório de casamentos para validar os IDs.
     * @throws IOException Se houver um erro de leitura do arquivo.
     * @throws ParseException Se houver um erro na conversão de valores numéricos.
     */
    public void carregarDados(String caminhoArquivo, CasamentoRepository casamentoRepo) throws IOException, ParseException {
        List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));

        for (String[] campos : linhas) {
            if (campos.length < 8) { // Verifica se há campos suficientes
                System.err.println("Linha inválida encontrada, ignorando: " + String.join(";", campos));
                continue;
            }

            // Informações básicas da festa
            String idFesta = campos[0].trim();

            // Verifica se o ID já existe no repositório
            if (this.festas.containsKey(idFesta)) {
                throw new IllegalArgumentException("ID repetido " + idFesta + " na classe Festa.");
            }

            String idCasamento = campos[1].trim();
            String local = campos[2].trim();
            LocalDate data = LocalDate.parse(campos[3].trim(), formatter);
            String hora = campos[4].trim();
            double valorFesta = numberFormat.parse(campos[5].trim()).doubleValue();
            int numParcelas = Integer.parseInt(campos[6].trim());
            int numConvidados = Integer.parseInt(campos[7].trim());

            // 🔹 Validação: Verifica se o ID do Casamento existe
            Casamento casamento = casamentoRepo.buscarPorId(idCasamento);
            if (casamento == null) {
                throw new IllegalArgumentException("ID(s) de Casamento " + idCasamento + " não cadastrado na Festa de ID " + idFesta + ".");
            }

            // Lista de convidados
            List<String> convidados = null;
            if (numConvidados > 0) { // Verifica se há convidados
                convidados = new ArrayList<>();
                for (int i = 8; i < campos.length; i++) {
                    // Adiciona o nome do convidado à lista
                    convidados.add(campos[i].trim());
                }
            } else { // Não há convidados
                System.out.println("A festa " + idFesta + " não possui convidados.");
            }

            // Cria a festa e a adiciona ao repositório
            Festa festa = new Festa(idFesta, idCasamento, local, valorFesta, numParcelas, data, hora, convidados);
            this.adicionar(festa);
        }
    }
}
