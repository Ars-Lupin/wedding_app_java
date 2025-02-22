package repository;

import model.Financeiro;
import model.Pessoa;
import model.PessoaFisica;
import model.PessoaJuridica;
import model.Loja;

import util.CSVReader;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Collection;

import java.text.NumberFormat;
import java.text.ParseException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Classe que representa um repositório de pessoas.
 */
public class PessoaRepository {

    private final Map<String, Pessoa> pessoas;

    /**
     * Construtor: inicializa o mapa de pessoas.
     */
    public PessoaRepository() {
        this.pessoas = new HashMap<>();
    }

    /**
     * Adiciona uma pessoa ao repositório.
     * 
     * @param pessoa Pessoa a ser adicionada.
     * @throws IllegalArgumentException Se a pessoa for nula ou já existir no repositório.
     */
    public void adicionar(Pessoa pessoa) {
        if (pessoa == null) {
            throw new IllegalArgumentException("A pessoa não pode ser nula.");
        }
        if (pessoas.containsKey(pessoa.getIdPessoa())) {
            throw new IllegalArgumentException("Já existe uma pessoa com este ID no repositório.");
        }
        this.pessoas.put(pessoa.getIdPessoa(), pessoa);
    }

    /**
     * Remove uma pessoa do repositório.
     * 
     * @param pessoa Pessoa a ser removida.
     * @throws IllegalArgumentException Se a pessoa for nula ou não existir no repositório.
     */
    public void remover(Pessoa pessoa) {
        if (pessoa == null) {
            throw new IllegalArgumentException("A pessoa não pode ser nula.");
        }
        if (!pessoas.containsKey(pessoa.getIdPessoa())) {
            throw new IllegalArgumentException("A pessoa não existe no repositório.");
        }
        this.pessoas.remove(pessoa.getIdPessoa());
    }

    /**
     * Lista todas as pessoas no repositório.
     * 
     * @return Coleção de pessoas.
     */
    public Collection<Pessoa> listar() {
        return this.pessoas.values();
    }

    /**
     * Busca uma pessoa pelo ID.
     * 
     * @param id ID da pessoa.
     * @return A pessoa correspondente ao ID, ou null se não for encontrada.
     * @throws IllegalArgumentException Se o ID for nulo ou inválido.
     */
    public Pessoa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.pessoas.get(id);
    }

    /**
     * Carrega os dados do arquivo pessoas.CSV e adiciona as pessoas ao repositório.
     *
     * @param caminhoArquivo Caminho do arquivo CSV.
     * @throws IOException Se houver erro na leitura do arquivo.
     * @throws ParseException Se houver erro na conversão de valores numéricos.
     */
    public void carregarDadosDoCSV(String caminhoArquivo) throws IOException, ParseException {
        List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));

        for (String[] campos : linhas) {

            // Informações abaixo são comuns a todos os tipos de pessoa
            // (Física, Jurídica e Loja)
            String id = campos[0].trim();
            String tipo = campos[1].trim();
            String nome = campos[2].trim();
            String telefone = campos[3].trim();
            String endereco = campos[4].trim();

            if (tipo.equals("F")) {
                if (campos.length < 10) { // Verifica se há campos suficientes
                    System.err.println("Linha inválida encontrada, ignorando: " + String.join(";", campos));
                    continue;
                }

                // Informações específicas de Pessoa Física
                String cpf = campos[5].trim();
                LocalDate dataNasc = LocalDate.parse(campos[6].trim(), formatter);

                // Converte valores financeiros usando NumberFormat
                double dinheiroPoupanca = numberFormat.parse(campos[7].trim()).doubleValue();
                double salarioLiquido = numberFormat.parse(campos[8].trim()).doubleValue();
                double gastosMensais = numberFormat.parse(campos[9].trim()).doubleValue();
                Financeiro financeiro = new Financeiro(dinheiroPoupanca, salarioLiquido, gastosMensais);

                PessoaFisica pessoaFisica = new PessoaFisica(nome, telefone, endereco, cpf, dataNasc, financeiro, id);

                this.adicionar(pessoaFisica); // Adiciona a pessoa física ao repositório
            } else if (tipo.equals("J") || tipo.equals("L")) { // Pessoa Jurídica ou Loja (Loja é uma pessoa jurídica)
                if (campos.length < 6) { // Verifica se há campos suficientes
                    System.err.println("Linha inválida encontrada, ignorando: " + String.join(";", campos));
                    continue;
                }

                // Informações específicas de Pessoa Jurídica
                String cnpj = campos[5].trim();

                if (tipo.equals("J")) {
                    // Pessoa Jurídica
                    PessoaJuridica pessoaJuridica = new PessoaJuridica(nome, telefone, endereco, cnpj, id);
                    this.adicionar(pessoaJuridica); // Adiciona a pessoa jurídica ao repositório
                } else if (tipo.equals("L")) {
                    // Loja (Pessoa Jurídica)
                    Loja loja = new Loja(nome, telefone, endereco, cnpj, id);
                    this.adicionar(loja); // Adiciona a loja ao repositório
                }
            } else {
                System.err.println("Tipo de pessoa inválido encontrado, ignorando: " + tipo);
                continue;
            }
        }
    }
}
