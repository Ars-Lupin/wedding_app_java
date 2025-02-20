package service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import model.Casamento;
import model.Compra;
import model.Festa;
import model.Financeiro;
import model.Lar;
import model.PessoaFisica;
import model.Tarefa;
import repository.CasamentoRepository;
import repository.CompraRepository;
import repository.FestaRepository;
import repository.LarRepository;
import repository.PessoaRepository;
import repository.TarefaRepository;

/**
 * Classe para gerar o planejamento financeiro de um casal baseado nos CPFs.
 */
public class PlanejamentoFinanceiro {

    private static final String SEPARADOR = ";";
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("MM/yyyy");

    private final CasamentoRepository casamentoRepo;
    private final PessoaRepository pessoaRepo;
    private final TarefaRepository tarefaRepo;
    private final FestaRepository festaRepo;
    private final CompraRepository compraRepo;
    private final LarRepository larRepo;

    public PlanejamentoFinanceiro(CasamentoRepository casamentoRepo, PessoaRepository pessoaRepo,
            TarefaRepository tarefaRepo, FestaRepository festaRepo, CompraRepository compraRepo,
            LarRepository larRepo) {
        this.casamentoRepo = casamentoRepo;
        this.pessoaRepo = pessoaRepo;
        this.tarefaRepo = tarefaRepo;
        this.festaRepo = festaRepo;
        this.compraRepo = compraRepo;
        this.larRepo = larRepo;

    }

    private void escreverCSV(String filePath, PessoaFisica pessoa1, PessoaFisica pessoa2,
            Map<String, Double> saldoMensal, LocalDate dataInicio, LocalDate dataFim) {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("MM/yyyy");

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {

            // Se o arquivo não existe, escrevemos o cabeçalho uma única vez

                writer.append("Nome 1;Nome 2;");

                // Adiciona todas as datas no cabeçalho, do primeiro mês até o último
                LocalDate dataAtual = dataInicio;
                while (!dataAtual.isAfter(dataFim)) {
                    writer.append(dataAtual.format(formatador)).append(";");
                    dataAtual = dataAtual.plusMonths(1);
                }
                writer.append("\n");

            // Escrever valores do saldo mensal para o casal
            writer.append(pessoa1.getNome()).append(";")
                    .append(pessoa2.getNome()).append(";");

            // Garante que os meses sem saldo também apareçam no arquivo
            dataAtual = dataInicio;
            while (!dataAtual.isAfter(dataFim)) {
                String chaveMes = dataAtual.format(formatador);
                double saldo = saldoMensal.getOrDefault(chaveMes, 0.0);
                writer.append(String.format("R$ %.2f;", saldo));
                dataAtual = dataAtual.plusMonths(1);
            }
            writer.append("\n");


        } catch (IOException e) {
            System.err.println("Erro ao escrever o arquivo CSV: " + e.getMessage());
        }
    }

    public void gerarPlanejamento(String filePath, String cpf1, String cpf2) {
        PessoaFisica pessoa1 = buscarPessoaPorCpf(cpf1);
        PessoaFisica pessoa2 = buscarPessoaPorCpf(cpf2);
        if (pessoa1 == null || pessoa2 == null) {
            escreverMensagem(filePath, "Casal com CPFs " + cpf1 + " e " + cpf2 + " não está cadastrado.");
            return;
        }

        Casamento casamento = buscarCasamentoDoCasal(pessoa1.getIdPessoa(), pessoa2.getIdPessoa());

        if (casamento == null) {
            escreverMensagem(filePath, "Casal com CPFs " + cpf1 + " e " + cpf2 + " não possui gastos cadastrados.");
            return;
        }

        Map<String, Double> saldoMensal = calcularSaldoMensal(casamento, pessoa1, pessoa2);
        LocalDate dataInicial = encontrarPrimeiroGasto(casamento);
        LocalDate dataFinal = calcularDataFinal(casamento, pessoa1.getIdPessoa(), pessoa2.getIdPessoa());

        if (saldoMensal.isEmpty()) {
            escreverMensagem(filePath, "Casal com CPFs " + cpf1 + " e " + cpf2 + " não possui gastos cadastrados.");
            return;
        }

        escreverCSV(filePath, pessoa1, pessoa2, saldoMensal, dataInicial, dataFinal);
    }

    private PessoaFisica buscarPessoaPorCpf(String cpf) {
        return pessoaRepo.listar().stream()
                .filter(p -> p instanceof PessoaFisica)
                .map(p -> (PessoaFisica) p)
                .filter(p -> p.getCpf().equals(cpf))
                .findFirst().orElse(null);
    }

    private Casamento buscarCasamentoDoCasal(String idPessoa1, String idPessoa2) {
        return casamentoRepo.listar().stream()
                .filter(c -> (c.getIdPessoa1().equals(idPessoa1) && c.getIdPessoa2().equals(idPessoa2)) ||
                        (c.getIdPessoa1().equals(idPessoa2) && c.getIdPessoa2().equals(idPessoa1)))
                .findFirst().orElse(null);
    }

    private void escreverMensagem(String filePath, String mensagem) {
        try (FileWriter writer = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            writer.write(mensagem + "\n");
            System.out.println(mensagem);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo CSV: " + e.getMessage());
        }
    }

    private Map<String, Double> calcularSaldoMensal(Casamento casamento, PessoaFisica pessoa1, PessoaFisica pessoa2) {
        Map<String, Double> saldoMensal = new LinkedHashMap<>();

        Financeiro financeiro1 = pessoa1.getFinanceiro();
        Financeiro financeiro2 = pessoa2.getFinanceiro();

        double saldo = financeiro1.getDinheiroPoupanca() + financeiro2.getDinheiroPoupanca();
        double salarioMensal = financeiro1.getSalarioLiquido() + financeiro2.getSalarioLiquido();

        LocalDate primeiraData = encontrarPrimeiroGasto(casamento);
        if (primeiraData == null)
            return Collections.emptyMap();

        LocalDate dataAtual = primeiraData;
        int mesesSemGastos = 0; // Contador de meses sem gastos para evitar loop infinito

        while (saldo >= 0 || gastosExistemAte(dataAtual)) {
            double gastosMensais = calcularGastosPorMes(casamento, dataAtual, pessoa1.getIdPessoa(),
                    pessoa2.getIdPessoa());
            saldo += salarioMensal - gastosMensais;

            if (gastosMensais == 0) {
                mesesSemGastos++;
            } else {
                mesesSemGastos = 0; // Reinicia o contador se houver gastos no mês
            }

            // 🔹 Se já se passaram 12 meses sem gastos, paramos para evitar loop infinito
            if (mesesSemGastos >= 12)
                break;

            // Simulação de rendimento da poupança
            if (saldo > 0) {
                saldo *= 1.005;
                saldo = Math.round(saldo * 100.0) / 100.0;
            }

            saldoMensal.put(dataAtual.format(FORMATADOR_DATA), saldo);
            dataAtual = dataAtual.plusMonths(1);
        }

        return saldoMensal;
    }

    private LocalDate encontrarPrimeiroGasto(Casamento casamento) {
        List<LocalDate> datas = new ArrayList<>();
        String idPessoa1 = casamento.getIdPessoa1();
        String idPessoa2 = casamento.getIdPessoa2();

        // Buscar tarefas associadas ao casal
        tarefaRepo.listar().stream()
                .filter(tarefa -> {
                    // Busca o lar da tarefa e verifica se pertence ao casal
                    Lar lar = larRepo.buscarPorId(tarefa.getIdLar());
                    return lar != null
                            && ((lar.getIdPessoa1().equals(idPessoa1) && lar.getIdPessoa2().equals(idPessoa2))
                                    || (lar.getIdPessoa1().equals(idPessoa2) && lar.getIdPessoa2().equals(idPessoa1)));
                })
                .map(Tarefa::getDataInicio)
                .forEach(datas::add);

        // Buscar festas associadas ao casal
        festaRepo.listar().stream()
                .filter(f -> f.getIdCasamento().equals(casamento.getIdCasamento()))
                .map(Festa::getData)
                .forEach(datas::add);

        // Buscar compras associadas a tarefas do casal
        compraRepo.listar().stream()
                .filter(compra -> {
                    String idTarefa = compra.getIdTarefa();
                    return tarefaRepo.buscarPorId(idTarefa).getIdLar() != null &&
                            larRepo.buscarPorId(tarefaRepo.buscarPorId(idTarefa).getIdLar()).getIdPessoa1()
                                    .equals(idPessoa1)
                            &&
                            larRepo.buscarPorId(tarefaRepo.buscarPorId(idTarefa).getIdLar()).getIdPessoa2()
                                    .equals(idPessoa2);
                })
                .map(c -> tarefaRepo.buscarPorId(c.getIdTarefa()).getDataInicio())
                .forEach(datas::add);

        return datas.stream().min(LocalDate::compareTo).orElse(null); // Retorna a menor data encontrada
    }

    private boolean pertenceAoCasal(String idPessoa1, String idPessoa2, String idLar) {
        if (idLar == null)
            return false;

        Lar lar = larRepo.buscarPorId(idLar);
        if (lar == null)
            return false;

        return (lar.getIdPessoa1().equals(idPessoa1) && lar.getIdPessoa2().equals(idPessoa2))
                || (lar.getIdPessoa1().equals(idPessoa2) && lar.getIdPessoa2().equals(idPessoa1));
    }

    private boolean gastosExistemAte(LocalDate data) {
        return tarefaRepo.listar().stream().anyMatch(t -> !t.getDataInicio().isAfter(data))
                || festaRepo.listar().stream().anyMatch(f -> !f.getData().isAfter(data))
                || compraRepo.listar().stream()
                        .map(Compra::getIdTarefa)
                        .map(tarefaRepo::buscarPorId)
                        .anyMatch(t -> !t.getDataInicio().isAfter(data));
    }

    private double calcularGastosPorMes(Casamento casamento, LocalDate data, String idPessoa1, String idPessoa2) {
        double total = 0;

        total += tarefaRepo.listar().stream()
        .filter(tarefa -> {
            Lar lar = larRepo.buscarPorId(tarefa.getIdLar());
            return lar != null
                    && ((lar.getIdPessoa1().equals(idPessoa1) && lar.getIdPessoa2().equals(idPessoa2))
                    || (lar.getIdPessoa1().equals(idPessoa2) && lar.getIdPessoa2().equals(idPessoa1)))
                    && tarefa.getDataInicio().equals(data);
        })
        .mapToDouble(Tarefa::getValorPrestador)
        .sum();


        total += festaRepo.listar().stream()
                .filter(festa -> festa.getIdCasamento().equals(casamento.getIdCasamento())
                        && festa.getData().equals(data))
                .mapToDouble(Festa::getValorFesta).sum();

        total += compraRepo.listar().stream()
                .filter(compra -> tarefaRepo.buscarPorId(compra.getIdTarefa()).getDataInicio().equals(data))
                .mapToDouble(compra -> compra.getQuantidade() * compra.getValorUnitario()).sum();

        return total;
    }

    private LocalDate calcularDataFinal(Casamento casamento, String idPessoa1, String idPessoa2) {
        LocalDate dataFinal = LocalDate.MIN;

        LocalDate ultimaDataTarefa = tarefaRepo.listar().stream()
                .filter(tarefa -> {
                    // Busca o lar da tarefa e verifica se pertence ao casal
                    Lar lar = larRepo.buscarPorId(tarefa.getIdLar());
                    return lar != null
                            && ((lar.getIdPessoa1().equals(idPessoa1) && lar.getIdPessoa2().equals(idPessoa2))
                                    || (lar.getIdPessoa1().equals(idPessoa2) && lar.getIdPessoa2().equals(idPessoa1)));
                })
                .map(tarefa -> tarefa.getDataInicio().plusMonths(tarefa.getNumParcelas() - 1)) // Considera as parcelas
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);


        // 🔹 Última data de qualquer festa associada ao casal
        LocalDate ultimaDataFesta = festaRepo.listar().stream()
                .filter(festa -> festa.getIdCasamento().equals(casamento.getIdCasamento()))
                .map(Festa::getData)
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);


        // 🔹 Última data de qualquer compra parcelada associada ao casal
        LocalDate ultimaDataCompra = compraRepo.listar().stream()
                .filter(compra -> {
                    String idTarefa = compra.getIdTarefa();
                    return tarefaRepo.buscarPorId(idTarefa).getIdLar() != null &&
                            larRepo.buscarPorId(tarefaRepo.buscarPorId(idTarefa).getIdLar()).getIdPessoa1()
                                    .equals(idPessoa1)
                            &&
                            larRepo.buscarPorId(tarefaRepo.buscarPorId(idTarefa).getIdLar()).getIdPessoa2()
                                    .equals(idPessoa2);
                })
                .map(compra -> {
                    Tarefa tarefa = tarefaRepo.buscarPorId(compra.getIdTarefa());
                    return tarefa.getDataInicio().plusMonths(compra.getNumParcelas() - 1); // Considera parcelas
                })
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);



        // 🔹 Determina a maior data entre todas
        if (!ultimaDataTarefa.equals(LocalDate.MIN)) {
            dataFinal = ultimaDataTarefa;
        }
        if (!ultimaDataFesta.equals(LocalDate.MIN) && ultimaDataFesta.isAfter(dataFinal)) {
            dataFinal = ultimaDataFesta;
        }
        if (!ultimaDataCompra.equals(LocalDate.MIN) && ultimaDataCompra.isAfter(dataFinal)) {
            dataFinal = ultimaDataCompra;
        }

        // 🔹 Se não houver despesas, retorna a data inicial + 1 mês como fallback
        return (dataFinal.equals(LocalDate.MIN)) ? LocalDate.now().plusMonths(1) : dataFinal;
    }

}
