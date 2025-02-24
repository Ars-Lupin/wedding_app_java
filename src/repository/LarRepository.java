package repository;

import java.io.IOException;
import java.util.*;
import model.Endereco;
import model.Lar;
import util.CSVReader;

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
     * @param lar Lar a ser adicionado.
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
     * @param lar Lar a ser removido.
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
     * @param id ID do lar.
     * @return O lar com o ID especificado.
     */
    public Lar buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.lares.get(id);
    }

    /**
     * Carrega os dados do arquivo `lares.csv` e adiciona os lares ao repositório.
     *
     * @param caminhoArquivo Caminho do arquivo CSV.
     * @param pessoaRepo    Repositório de pessoas para validação dos IDs.
     * @throws IOException Se houver um erro de leitura do arquivo.
     */
    public void carregarDados(String caminhoArquivo, PessoaRepository pessoaRepo) throws IOException {
        List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);

        for (String[] campos : linhas) {
            if (campos.length < 6) { // Verifica se há campos suficientes
                System.err.println("Linha inválida encontrada, ignorando: " + String.join(";", campos));
                continue;
            }

            // Informações dos IDs que envolvem um lar
            String idLar = campos[0].trim();

            // Verifica se o ID já existe no repositório
            if (this.lares.containsKey(idLar)) {
                throw new IllegalArgumentException("ID repetido " + idLar + " na classe Lar.");
            }

            String idPessoa1 = campos[1].trim();
            String idPessoa2 = campos[2].trim();

            // 🔹 Validação: verificar se as pessoas existem no `PessoaRepository`
            boolean pessoa1Existe = pessoaRepo.buscarPorId(idPessoa1) != null;
            boolean pessoa2Existe = pessoaRepo.buscarPorId(idPessoa2) != null;

            if (!pessoa1Existe && !pessoa2Existe) {
                throw new IllegalArgumentException(
                        "ID(s) de Pessoa " + idPessoa1 + " " + idPessoa2 +
                                " não cadastrado(s) no Lar de ID " + idLar + ".");
            }
            if (!pessoa1Existe) {
                throw new IllegalArgumentException(
                        "ID de Pessoa " + idPessoa1 + " não cadastrado no Lar de ID " + idLar + ".");
            }
            if (!pessoa2Existe) {
                throw new IllegalArgumentException(
                        "ID de Pessoa " + idPessoa2 + " não cadastrado no Lar de ID " + idLar + ".");
            }

            // Endereço do lar
            String rua = campos[3].trim();
            int num = Integer.parseInt(campos[4].trim());
            String complemento = campos[5].trim();
            Endereco endereco = new Endereco(rua, num, complemento);

            // Cria e adiciona o novo lar ao repositório
            Lar lar = new Lar(idLar, idPessoa1, idPessoa2, endereco);
            this.adicionar(lar); // Aqui a validação de IDs de pessoa será feita
        }
    }
}
