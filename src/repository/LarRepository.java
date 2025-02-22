package repository;

import model.Lar;
import model.Endereco;

import util.CSVReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

import java.io.IOException;

/**
 * Classe que representa um repositório de lares.
 */
public class LarRepository {

    private final Map<String, Lar> lares;

    /**
     * Construtor: inicializa o mapa de lares.
    */
    public LarRepository() {
        this.lares = new HashMap<>();
    }

    /**
     * Adiciona um lar ao repositório.
     * 
     * @param lar
     * @throws IllegalArgumentException Se o lar for nulo ou já existir no repositório.
     */
    public void adicionar(Lar lar) {
        if (lar == null) {
            throw new IllegalArgumentException("O lar não pode ser nulo.");
        }
        if (lares.containsKey(lar.getIdLar())) {
            throw new IllegalArgumentException("Já existe um lar com este ID no repositório.");
        }
        this.lares.put(lar.getIdLar(), lar);
    }

    /**
     * Remove um lar do repositório.
     * 
     * @param lar
     * @throws IllegalArgumentException Se o lar for nulo ou não existir no repositório.
     */
    public void remover(Lar lar) {
        if (lar == null) {
            throw new IllegalArgumentException("O lar não pode ser nulo.");
        }
        if (!lares.containsKey(lar.getIdLar())) {
            throw new IllegalArgumentException("O lar não existe no repositório.");
        }
        this.lares.remove(lar.getIdLar());
    }

    /**
     * Lista todos os lares do repositório.
     * 
     * @return Uma coleção com todos os lares do repositório.
     */
    public Collection<Lar> listar() {
        return this.lares.values();
    }

    /**
     * Busca um lar pelo ID.
     * 
     * @param id
     * @return O lar com o ID especificado.
     * @throws IllegalArgumentException Se o ID for nulo ou vazio.
     */
    public Lar buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.lares.get(id);
    }

    /**
     * Carrega os dados do arquivo lares.CSV e adiciona os lares ao repositório.
     * 
     * @param caminhoArquivo
     * @throws IOException Se houver um erro de leitura do arquivo.
     */
    public void carregarDados(String caminhoArquivo) throws IOException {
        List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);

        for (String[] campos : linhas) {
            if (campos.length < 6) { // Verifica se há campos suficientes
                System.err.println("Linha inválida encontrada, ignorando: " + String.join(";", campos));
                continue;
            }

            // Informações dos IDs que envolvem um lar
            String idLar = campos[0].trim();
            String idPessoa1 = campos[1].trim();
            String idPessoa2 = campos[2].trim();

            // Endereço de um lar
            String rua = campos[3].trim();
            int num = Integer.parseInt(campos[4].trim());
            String complemento = campos[5].trim();
            Endereco endereco = new Endereco(rua, num, complemento);

            // Cria e adiciona o novo lar ao repositório
            Lar lar = new Lar(idLar, idPessoa1, idPessoa2, endereco);
            this.adicionar(lar);
        }
    }
}
