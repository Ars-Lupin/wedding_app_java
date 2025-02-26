package service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import model.Casamento;
import model.Lar;
import model.PessoaFisica;
import repository.CasamentoRepository;
import repository.CompraRepository;
import repository.FestaRepository;
import repository.LarRepository;
import repository.PessoaRepository;
import repository.TarefaRepository;

/**
 * Classe para gerar estatísticas financeiras dos casais cadastrados.
 */
public class EstatisticasCasaisService {

    private static final String SEPARADOR = ";";

    private final CasamentoRepository casamentoRepo;
    private final PessoaRepository pessoaRepo;
    private final TarefaRepository tarefaRepo;
    private final FestaRepository festaRepo;
    private final CompraRepository compraRepo;
    private final LarRepository larRepo;

    public EstatisticasCasaisService(CasamentoRepository casamentoRepo, PessoaRepository pessoaRepo,
                              TarefaRepository tarefaRepo, FestaRepository festaRepo, CompraRepository compraRepo,
                              LarRepository larRepo) {
        this.casamentoRepo = casamentoRepo;
        this.pessoaRepo = pessoaRepo;
        this.tarefaRepo = tarefaRepo;
        this.festaRepo = festaRepo;
        this.compraRepo = compraRepo;
        this.larRepo = larRepo;
    }

    /**
     * Gera um relatório com as estatísticas dos casais.
     *
     * @param filePath Caminho do arquivo CSV de saída.
     */
    public void gerarEstatisticas(String filePath) {
        List<EstatisticaCasal> estatisticasLista = new ArrayList<>();
        Set<String> casaisProcessados = new HashSet<>(); // Evita duplicatas
    
        // 🔹 Busca casais em `LarRepository`
        for (Lar lar : larRepo.listar()) {
            adicionarEstatisticaCasal(lar.getIdPessoa1(), lar.getIdPessoa2(), estatisticasLista, casaisProcessados);
        }
    
        // 🔹 Busca casais em `CasamentoRepository`, garantindo que não estejam duplicados
        for (Casamento casamento : casamentoRepo.listar()) {
            adicionarEstatisticaCasal(casamento.getIdPessoa1(), casamento.getIdPessoa2(), estatisticasLista, casaisProcessados);
        }
    
        // 🔹 Ordenação: primeiro pelo total gasto (decrescente), depois pelo nome1 (alfabético)
        estatisticasLista.sort(Comparator.comparingDouble(EstatisticaCasal::getTotalGasto).reversed()
                .thenComparing(EstatisticaCasal::getNome1));
    
        // 🔹 Escrevendo os dados no CSV
        try (FileWriter writer = new FileWriter(filePath, StandardCharsets.UTF_8)) {
            for (EstatisticaCasal estatistica : estatisticasLista) {
                writer.append(estatistica.getNome1()).append(SEPARADOR)
                      .append(estatistica.getNome2()).append(SEPARADOR)
                      .append(String.format("R$ %.2f", estatistica.getTotalGasto())).append(SEPARADOR)
                      .append(String.valueOf(estatistica.getFestasConvidados()))
                      .append("\n");
            }
        } catch (IOException e) {
            System.out.println("Erro ao escrever o arquivo CSV: " + e.getMessage());
        }
    }
    
    /**
     * Adiciona um casal às estatísticas apenas se ele ainda não foi processado.
     */
    private void adicionarEstatisticaCasal(String idPessoa1, String idPessoa2, List<EstatisticaCasal> estatisticasLista, Set<String> casaisProcessados) {
        String chaveCasal = idPessoa1 + "-" + idPessoa2; // 🔹 Evita duplicatas
    
        if (casaisProcessados.contains(chaveCasal)) {
            return;
        }
        casaisProcessados.add(chaveCasal);
    
        PessoaFisica pessoa1 = (PessoaFisica) pessoaRepo.buscarPorId(idPessoa1);
        PessoaFisica pessoa2 = (PessoaFisica) pessoaRepo.buscarPorId(idPessoa2);
    
        if (pessoa1 == null || pessoa2 == null) return;
    
        // Ordenação dos nomes para manter consistência
        String nome1 = pessoa1.getNome().compareToIgnoreCase(pessoa2.getNome()) < 0 ? pessoa1.getNome() : pessoa2.getNome();
        String nome2 = pessoa1.getNome().compareToIgnoreCase(pessoa2.getNome()) < 0 ? pessoa2.getNome() : pessoa1.getNome();
    
        // Calculando os gastos
        double totalGasto = calcularGastosTarefas(idPessoa1, idPessoa2)
                          + calcularGastosFestas(idPessoa1, idPessoa2)
                          + calcularGastosCompras(idPessoa1, idPessoa2);
    
        // Verifica quantas festas o casal foi convidado
        int festasConvidados = (int) festaRepo.listar().stream()
                .filter(festa -> festa.getConvidados().contains(nome1) && festa.getConvidados().contains(nome2))
                .count();
    
        estatisticasLista.add(new EstatisticaCasal(nome1, nome2, totalGasto, festasConvidados));
    }
    

    // 🔹 Função que soma os gastos com tarefas associadas ao casal, verificando os lares
    private double calcularGastosTarefas(String idPessoa1, String idPessoa2) {
        return tarefaRepo.listar().stream()
                .filter(tarefa -> {
                    // Busca o lar da tarefa e verifica se pertence ao casal
                    Lar lar = larRepo.buscarPorId(tarefa.getIdLar());
                    return lar != null && ((lar.getIdPessoa1().equals(idPessoa1) && lar.getIdPessoa2().equals(idPessoa2))
                                        || (lar.getIdPessoa1().equals(idPessoa2) && lar.getIdPessoa2().equals(idPessoa1)));
                })
                .mapToDouble(tarefa -> tarefa.getValorPrestador())
                .sum();
    }

    // 🔹 Função que soma os gastos com festas associadas ao casal
    private double calcularGastosFestas(String idPessoa1, String idPessoa2) {
        return festaRepo.listar().stream()
                .filter(festa -> {
                    String idCasamento = festa.getIdCasamento();
                    Casamento casamento = casamentoRepo.buscarPorId(idCasamento);
                    return casamento.getIdPessoa1().equals(idPessoa1) && casamento.getIdPessoa2().equals(idPessoa2);
                })
                .mapToDouble(festa -> festa.getValorFesta())
                .sum();
    }

    // 🔹 Função que soma os gastos com compras associadas ao casal
    private double calcularGastosCompras(String idPessoa1, String idPessoa2) {
        return compraRepo.listar().stream()
                .filter(compra -> {
                    String idTarefa = compra.getIdTarefa();
                    return tarefaRepo.buscarPorId(idTarefa).getIdLar() != null && 
                        larRepo.buscarPorId(tarefaRepo.buscarPorId(idTarefa).getIdLar()).getIdPessoa1().equals(idPessoa1) &&
                        larRepo.buscarPorId(tarefaRepo.buscarPorId(idTarefa).getIdLar()).getIdPessoa2().equals(idPessoa2);
                })
                .mapToDouble(compra -> compra.getQuantidade() * compra.getValorUnitario())
                .sum();
    }
}

/**
 * Classe auxiliar para armazenar estatísticas de um casal.
 */
class EstatisticaCasal {
    private final String nome1;
    private final String nome2;
    private final double totalGasto;
    private final int festasConvidados;

    public EstatisticaCasal(String nome1, String nome2, double totalGasto, int festasConvidados) {
        this.nome1 = nome1;
        this.nome2 = nome2;
        this.totalGasto = totalGasto;
        this.festasConvidados = festasConvidados;
    }

    public String getNome1() {
        return nome1;
    }

    public String getNome2() {
        return nome2;
    }

    public double getTotalGasto() {
        return totalGasto;
    }

    public int getFestasConvidados() {
        return festasConvidados;
    }
}
