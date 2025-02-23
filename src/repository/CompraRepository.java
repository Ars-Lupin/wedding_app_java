package repository;

import model.Compra;
import util.CSVReader;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Collection;

import java.text.NumberFormat;
import java.text.ParseException;

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
     * 
     * @param compra
     * @throws IllegalArgumentException Se a compra for nula ou já existir
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

    /**
     * Remove uma Compra do repositório
     * 
     * @param compra
     * @throws IllegalArgumentException Se a compra for nula ou não existir
     */
    public void remover(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("A compra não pode ser nula.");
        }
        if (!compras.containsKey(compra.getIdCompra())) {
            throw new IllegalArgumentException("A compra não existe no repositório.");
        }
        this.compras.remove(compra.getIdCompra());
    }

    /**
     * Lista todas as Compras do repositório
     * 
     * @return Collection<Compra>
     */
    public Collection<Compra> listar() {
        return this.compras.values();
    }

    /**
     * Busca e retorna uma compra pelo seu ID
     * 
     * @param caminhoArquivo
     * @throws IOException Se houver erro na leitura do arquivo
     */
    public Compra buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.compras.get(id);
    }

    /**
     * Carrega os dados do arquivo compras.CSV e adiciona as compras ao repositório
     * 
     * @param caminhoArquivo
     * @throws IOException Se houver erro na leitura do arquivo
     * @throws ParseException Se houver erro na conversão de valores numéricos
     */
    public void carregarDados(String caminhoArquivo) throws IOException, ParseException {
        List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));

        for (String[] campos : linhas) {
            if (campos.length < 7) { // Verifica se a linha contém todos os campos necessários
                System.err.println("Linha inválida encontrada, ignorando: " + String.join(";", campos));
                continue;
            }

            String idCompra = campos[0].trim();

            // Verifica se o ID já existe no repositório
            if (this.compras.containsKey(idCompra)) {
                throw new IllegalArgumentException("ID repetido " + idCompra + " na classe Compra.");
            }

            String idTarefa = campos[1].trim();
            String idLoja = campos[2].trim();
            String nomeProduto = campos[3].trim();
            int qtdProduto = Integer.parseInt(campos[4].trim());
            double valorUnitario = numberFormat.parse(campos[5].trim()).doubleValue();
            int numParcelas = Integer.parseInt(campos[6].trim());

            // Cria uma nova compra e a adiciona ao repositório
            Compra compra = new Compra(idCompra, idLoja, idTarefa, nomeProduto, qtdProduto, valorUnitario, numParcelas);
            this.adicionar(compra);
        }
    }
}
