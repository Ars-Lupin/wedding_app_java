package repository;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import model.Compra;
import model.Loja;
import model.Pessoa;
import model.PessoaJuridica;
import model.Tarefa;
import util.CSVReader;

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
     * @param compra Compra a ser adicionada.
     * @throws IllegalArgumentException Se a compra for nula ou já existir.
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
     * @param compra Compra a ser removida.
     * @throws IllegalArgumentException Se a compra for nula ou não existir.
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
     * @param id ID da compra.
     * @return A compra correspondente ao ID.
     * @throws IllegalArgumentException Se o ID for inválido.
     */
    public Compra buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.compras.get(id);
    }

    /**
     * Carrega os dados do arquivo compras.CSV e adiciona as compras ao repositório.
     * Agora inclui validações para IDs de tarefas e lojas.
     *
     * @param caminhoArquivo Caminho do arquivo CSV.
     * @param tarefaRepo Repositório de tarefas para validação de IDs.
     * @param pessoaRepo Repositório de pessoas para validar se a loja realmente existe.
     * @throws IOException Se houver erro na leitura do arquivo.
     * @throws ParseException Se houver erro na conversão de valores numéricos.
     */
    public void carregarDados(String caminhoArquivo, TarefaRepository tarefaRepo, PessoaRepository pessoaRepo) throws IOException, ParseException {
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

            // 🔹 Validação: Verifica se o ID da Tarefa existe
            Tarefa tarefa = tarefaRepo.buscarPorId(idTarefa);
            if (tarefa == null) {
                throw new IllegalArgumentException("ID(s) de Tarefa " + idTarefa + " não cadastrado na Compra de ID " + idCompra + ".");
            }

            // 🔹 Validação: Verifica se a Loja existe e se é de fato uma Loja
            Pessoa pessoa = pessoaRepo.buscarPorId(idLoja);
            if (pessoa == null) {
                throw new IllegalArgumentException("ID(s) de Loja " + idLoja + " não cadastrado na Compra de ID " + idCompra + ".");
            }
            if (pessoa instanceof PessoaJuridica && !(pessoa instanceof Loja)) {
                throw new IllegalArgumentException("ID " + idLoja + " da Compra de ID " + idCompra + " não se refere a uma Loja, mas a uma Pessoa Jurídica.");
            }

            // Cria uma nova compra e a adiciona ao repositório
            Compra compra = new Compra(idCompra, idLoja, idTarefa, nomeProduto, qtdProduto, valorUnitario, numParcelas);
            this.adicionar(compra);
        }
    }
}
