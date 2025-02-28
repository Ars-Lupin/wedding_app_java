package repository;

import java.io.IOException;

import java.util.*;

import java.text.NumberFormat;
import java.text.ParseException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.*;

import util.CSVReader;

/**
 * Classe que representa um repositório de pessoas.
 */
public class PessoaRepository {

    private final Map<String, Pessoa> pessoas; // Mapa de ID → Pessoa
    private final Map<String, String> cpfs;    // Mapa de CPF → ID da Pessoa Física
    private final Map<String, String> cnpjs;   // Mapa de CNPJ → ID da Pessoa Jurídica

    /**
     * Construtor: inicializa os mapas de pessoas.
     */
    public PessoaRepository() {
        this.pessoas = new HashMap<>();
        this.cpfs = new HashMap<>();
        this.cnpjs = new HashMap<>();
    }

    /**
     * Adiciona uma pessoa ao repositório com verificações de CPF e CNPJ duplicados.
     *
     * @param pessoa Pessoa a ser adicionada.
     * @throws IllegalArgumentException Se a pessoa já existir no repositório ou se CPF/CNPJ for duplicado com ID diferente.
     */
    public void adicionar(Pessoa pessoa) {
        if (pessoa == null) {
            throw new IllegalArgumentException("A pessoa não pode ser nula.");
        }
        if (pessoas.containsKey(pessoa.getIdPessoa())) {
            throw new IllegalArgumentException("Já existe uma pessoa com este ID no repositório.");
        }

        // Validação para Pessoa Física (CPF)
        if (pessoa instanceof PessoaFisica) {
            PessoaFisica pf = (PessoaFisica) pessoa;
            if (cpfs.containsKey(pf.getCpf()) && !cpfs.get(pf.getCpf()).equals(pf.getIdPessoa())) {
                throw new IllegalArgumentException("O CPF " + pf.getCpf() + " da Pessoa " + pf.getIdPessoa() + " é repetido.");
            }
            cpfs.put(pf.getCpf(), pf.getIdPessoa()); // Adiciona ao mapa de CPFs
        }

        // Validação para Pessoa Jurídica e Loja (CNPJ)
        if (pessoa instanceof PessoaJuridica) {
            PessoaJuridica pj = (PessoaJuridica) pessoa;
            if (cnpjs.containsKey(pj.getCnpj()) && !cnpjs.get(pj.getCnpj()).equals(pj.getIdPessoa())) {
                throw new IllegalArgumentException("O CNPJ " + pj.getCnpj() + " da Pessoa " + pj.getIdPessoa() + " é repetido.");
            }
            cnpjs.put(pj.getCnpj(), pj.getIdPessoa()); // Adiciona ao mapa de CNPJs
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

        // Remove dos mapas auxiliares de CPF/CNPJ
        if (pessoa instanceof PessoaFisica) {
            cpfs.remove(((PessoaFisica) pessoa).getCpf());
        } else if (pessoa instanceof PessoaJuridica) {
            cnpjs.remove(((PessoaJuridica) pessoa).getCnpj());
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
     * @throws IllegalArgumentException Se o ID for nulo ou vazio.
     * @return A pessoa correspondente ao ID, ou null se não for encontrada.
     */
    public Pessoa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        return this.pessoas.get(id);
    }

    /**
     * Carrega os dados do arquivo pessoas.csv e adiciona as pessoas ao repositório.
     *
     * @param caminhoArquivo Caminho do arquivo CSV.
     * @throws IOException Se houver erro na leitura do arquivo.
     * @throws ParseException Se houver erro na conversão de valores numéricos.
     */
    public void carregarDadosDoCSV(String caminhoArquivo) throws IOException, ParseException {
        List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.of("pt", "BR"));

        for (String[] campos : linhas) {
            String id = campos[0].trim();
            if (this.pessoas.containsKey(id)) {
                throw new IllegalArgumentException("ID repetido " + id + " na classe Pessoa.");
            }

            String tipo = campos[1].trim();
            String nome = campos[2].trim();
            String telefone = campos[3].trim();
            String endereco = campos[4].trim();

            if (tipo.equals("F")) { // Pessoa Física
                if (campos.length < 10) continue;

                String cpf = campos[5].trim();
                LocalDate dataNasc = LocalDate.parse(campos[6].trim(), formatter);
                double dinheiroPoupanca = numberFormat.parse(campos[7].trim()).doubleValue();
                double salarioLiquido = numberFormat.parse(campos[8].trim()).doubleValue();
                double gastosMensais = numberFormat.parse(campos[9].trim()).doubleValue();
                Financeiro financeiro = new Financeiro(dinheiroPoupanca, salarioLiquido, gastosMensais);

                PessoaFisica pessoaFisica = new PessoaFisica(nome, telefone, endereco, cpf, dataNasc, financeiro, id);

                // Verifica se o CPF já existe com outro ID
                if (cpfs.containsKey(cpf) && !cpfs.get(cpf).equals(id)) {
                    throw new IllegalArgumentException("O CPF " + cpf + " da Pessoa " + id + " é repetido.");
                }

                this.adicionar(pessoaFisica);
            } else if (tipo.equals("J") || tipo.equals("L")) { // Pessoa Jurídica ou Loja
                if (campos.length < 6) continue;

                String cnpj = campos[5].trim();

                if (tipo.equals("J")) {
                    PessoaJuridica pessoaJuridica = new PessoaJuridica(nome, telefone, endereco, cnpj, id);
                    
                    // Verifica se o CNPJ já existe com outro ID
                    if (cnpjs.containsKey(cnpj) && !cnpjs.get(cnpj).equals(id)) {
                        throw new IllegalArgumentException("O CNPJ " + cnpj + " da Pessoa " + id + " é repetido.");
                    }

                    this.adicionar(pessoaJuridica);
                } else if (tipo.equals("L")) {
                    Loja loja = new Loja(nome, telefone, endereco, cnpj, id);
                    this.adicionar(loja);
                }
            } else {
                System.out.println("Tipo de pessoa inválido encontrado, ignorando: " + tipo);
            }
        }
    }
}
