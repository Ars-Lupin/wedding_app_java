package repository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import model.Casal;
import model.Casamento;
import model.Festa;
import util.CSVReader;
import model.Lar;

/**
 * Classe que representa um repositório de casamentos
 */
public class CasamentoRepository {

    private final Map<String, Casamento> casamentos;
    private final List<String> IDs;

    /**
     * Construtor: inicializa o mapa de casamentos
     */
    public CasamentoRepository() {
        this.casamentos = new HashMap<>();
        this.IDs = new ArrayList<>();
    }

    /**
     * Adiciona um casamento ao repositório
     *
     * @param casamento Casamento a ser adicionado.
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
     * @param casamento Casamento a ser removido.
     * @throws IllegalArgumentException Se o casamento for nulo ou não existir no repositório
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
     * @param id ID do casamento.
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
     * Carrega os dados do arquivo casamentos.CSV e adiciona os casamentos ao repositório,
     * validando se as pessoas envolvidas existem no sistema.
     *
     * @param caminhoArquivo Caminho do arquivo CSV.
     * @param pessoaRepo    Repositório de pessoas para validação dos IDs.
     * @param festaRepo     Repositório de festas para buscar a festa associada ao casamento.
     * @throws IOException Se houver um erro de leitura do arquivo.
     */
    public void carregarDados(String caminhoArquivo, PessoaRepository pessoaRepo, FestaRepository festaRepo, LarRepository larRepo, CasalRepository casalRepo) throws IOException {
        List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (String[] campos : linhas) {
            if (campos.length < 6) { // Verifica se há campos suficientes
                System.out.println("Linha inválida encontrada, ignorando: " + String.join(";", campos));
                continue;
            }

            // Informações dos IDs que envolvem um casamento
            String idCasamento = campos[0].trim();

            // Verifica se o ID já existe no repositório
            if (this.casamentos.containsKey(idCasamento)) {
                throw new IllegalArgumentException("ID repetido " + idCasamento + " na classe Casamento.");
            }

            String idPessoa1 = campos[1].trim();
            String idPessoa2 = campos[2].trim();
            LocalDate data = LocalDate.parse(campos[3].trim(), formatter);
            String hora = campos[4].trim();
            String local = campos[5].trim();

            // 🔹 Validação: verificar se as pessoas existem no `PessoaRepository`
            boolean pessoa1Existe = pessoaRepo.buscarPorId(idPessoa1) != null;
            boolean pessoa2Existe = pessoaRepo.buscarPorId(idPessoa2) != null;

            if (!pessoa1Existe && !pessoa2Existe) {
                throw new IllegalArgumentException(
                        "ID(s) de Pessoa " + idPessoa1 + " " + idPessoa2 +
                                " não cadastrado no Casamento de ID " + idCasamento + ".");
            }
            if (!pessoa1Existe) {
                throw new IllegalArgumentException(
                        "ID(s) de Pessoa " + idPessoa1 + " não cadastrado no Casamento de ID " + idCasamento + ".");
            }
            if (!pessoa2Existe) {
                throw new IllegalArgumentException(
                        "ID(s) de Pessoa " + idPessoa2 + " não cadastrado no Casamento de ID " + idCasamento + ".");
            }

            // 🔹 Busca a festa associada ao casamento (se houver)
            Festa festa = festaRepo.listar().stream()
                    .filter(f -> f.getIdCasamento().equals(idCasamento))
                    .findFirst()
                    .orElse(null);

            // Cria e adiciona o novo casamento ao repositório
            Casal casal = casalRepo.buscarPorIdPessoa(idPessoa1); // ou idPessoa2, tanto faz

            if (casal == null) {
                casal = new Casal(idPessoa1, idPessoa2, idCasamento, null);
                casalRepo.adicionar(casal);
            } else if (casal.getIdCasamento() == null) {
                casal.setIdCasamento(idCasamento);
            }

            Casamento casamento = new Casamento(idCasamento, casal, data, hora, local, festa);
            this.adicionar(casamento);
            this.IDs.add(idCasamento);
        }
    }

    public void recarregarFestas(FestaRepository festaRepo) {
        for (Casamento casamento : casamentos.values()) {
            // Tenta encontrar uma festa para este casamento
            Festa festa = festaRepo.buscarPorId(casamento.getIdCasamento());
            if (festa != null) {
                casamento.setFesta(festa);
            }
        }
    }

    public Map<String, Casamento> getCasamentos() {
        return casamentos;
    }

    public List<String> getIDs() {
        return IDs;
    }
}
