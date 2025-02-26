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
                while (true) {  
                    writer.append(dataAtual.format(formatador));
                    if (dataAtual.getYear() == dataFim.getYear() && dataAtual.getMonthValue() == dataFim.getMonthValue()) {
                        // Sai do loop apenas após uma iteração extra
                        break;
                    }

                    writer.append(";");

                    dataAtual = dataAtual.plusMonths(1);
                }

                writer.append("\n");

            // Escrever valores do saldo mensal para o casal
            writer.append(pessoa1.getNome()).append(";")
                    .append(pessoa2.getNome()).append(";");

            // Garante que os meses sem saldo também apareçam no arquivo
            dataAtual = dataInicio;
            while (true) {  
                String chaveMes = dataAtual.format(formatador);
                double saldo = saldoMensal.getOrDefault(chaveMes, 0.0);
                writer.append(String.format("R$ %.2f;", saldo));
                if (dataAtual.getYear() == dataFim.getYear() && dataAtual.getMonthValue() == dataFim.getMonthValue()) {
                    // Sai do loop apenas após uma iteração extra
                    break;
                }
                dataAtual = dataAtual.plusMonths(1);
            }
            writer.append("\n");

        } catch (IOException e) {
            System.out.println("Erro ao escrever o arquivo CSV: " + e.getMessage());
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

        Map<String, Double> saldoMensal = calcularSaldoMensalCasal(casamento, pessoa1, pessoa2);
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
            System.out.println("Erro ao escrever no arquivo CSV: " + e.getMessage());
        }
    }

    private Map<String, Double> calcularSaldoMensalCasal(Casamento casamento, PessoaFisica pessoa1, PessoaFisica pessoa2) {
        Map<String, Double> saldoMensal = new LinkedHashMap<>();

        LocalDate primeiraData = encontrarPrimeiroGasto(casamento);
        String idPessoa1 = casamento.getIdPessoa1();
        String idPessoa2 = casamento.getIdPessoa2();
        LocalDate ultimaData = calcularDataFinal(casamento, idPessoa1, idPessoa2);

        if (primeiraData == null || ultimaData == null) {
            return Collections.emptyMap();
        } 

        LocalDate dataAtual = primeiraData;

        // Informações financeiras do casal
        Financeiro financeiro1 = pessoa1.getFinanceiro();
        Financeiro financeiro2 = pessoa2.getFinanceiro();
        
        // No primeiro mes, o saldo mensal é a soma do dinheiro guardado do casal
        double saldoMensalCasal = financeiro1.getDinheiroPoupanca() + financeiro2.getDinheiroPoupanca();
        double salarioTotalCasalFixo = financeiro1.getSalarioLiquido() + financeiro2.getSalarioLiquido();
        double gastosMensaisCasalFixo = financeiro1.getGastosMensais() + financeiro2.getGastosMensais();
    
        while (true) {
            // Gastos mensais do casal (compras, tarefas festas) + gastos fixos de cada mensal
            double gastosTotais = gastosMensaisCasalFixo + calcularGastosPorMes(casamento.getIdCasamento(), 
                                                            dataAtual, pessoa1.getIdPessoa(), 
                                                            pessoa2.getIdPessoa());                     

            // Atualiza o saldo mensal do casal
            saldoMensalCasal += (saldoMensalCasal * (0.5/100)) + salarioTotalCasalFixo - gastosTotais;

            // Se for dezembro, adicionar 13o salário
            if (dataAtual.getMonthValue() == 12) {
                saldoMensalCasal += financeiro1.getSalarioLiquido() + financeiro2.getSalarioLiquido();
            }

            saldoMensal.put(dataAtual.format(FORMATADOR_DATA), saldoMensalCasal);

            if (dataAtual.getYear() == ultimaData.getYear() && dataAtual.getMonthValue() == ultimaData.getMonthValue()) {
                // Sai do loop apenas após uma iteração extra
                break;
            }

            dataAtual = dataAtual.plusMonths(1);
        }
    
        return saldoMensal;
    }

        /**
     * Calcula os gastos do casal em um determinado mês.
     *
     * @param idCasal     ID do casal
     * @param idCasamento ID do casamento (pode ser null)
     * @param data        Data do mês desejado (formato yyyy-MM)
     * @return Total de gastos no mês especificado
     */
    public double calcularGastosPorMes(String idCasamento, LocalDate data, String idPessoa1, String idPessoa2) {
        double totalGastos = 0.0;

        // Filtrar tarefas associadas ao casal e ao casamento (se houver)
        List<Tarefa> tarefas = new ArrayList<>(tarefaRepo.listar());
        for (Tarefa tarefa : tarefas) {
            // Verifica se a tarefa pertence ao lar do casal
            Lar larAssociado = larRepo.buscarPorId(tarefa.getIdLar());
            if (larAssociado != null 
                    && (larAssociado.getIdPessoa1().equals(idPessoa1)
                    && larAssociado.getIdPessoa2().equals(idPessoa2) 
                    || larAssociado.getIdPessoa1().equals(idPessoa2)
                    && larAssociado.getIdPessoa2().equals(idPessoa1))) {
                if (estaParcelaSendoPaga(tarefa.getDataInicio(), tarefa.getNumParcelas(), data)) {
                    totalGastos += tarefa.getValorParcela();
                }
            }
        }

        // Filtrar compras associadas ao casal e ao casamento (se houver)
        List<Compra> compras = new ArrayList<>(compraRepo.listar());
        for (Compra compra : compras) {
            Tarefa tarefaAssociada = tarefaRepo.buscarPorId(compra.getIdTarefa());
            Lar larAssociado = larRepo.buscarPorId(tarefaAssociada.getIdLar());
            if (larAssociado != null
                    && (larAssociado.getIdPessoa1().equals(idPessoa1)
                    && larAssociado.getIdPessoa2().equals(idPessoa2)
                    || larAssociado.getIdPessoa1().equals(idPessoa2)
                    && larAssociado.getIdPessoa2().equals(idPessoa1))) {
                LocalDate dataPrimeiroPagamento = tarefaAssociada.getDataInicio();
                if (estaParcelaSendoPaga(dataPrimeiroPagamento, compra.getNumParcelas(), data)) {
                    totalGastos += compra.getValorParcela();
                }
            }
        }

        // Filtrar festas associadas ao casamento (se houver)
        List<Festa> festas = new ArrayList<>(festaRepo.listar());
        for (Festa festa : festas) {
            Casamento casamentoAssociado = casamentoRepo.buscarPorId(festa.getIdCasamento());
            if (idCasamento != null && casamentoAssociado.getIdCasamento().equals(idCasamento)) {
                LocalDate dataPrimeiroPagamento = festa.getData();
                if (estaParcelaSendoPaga(dataPrimeiroPagamento, festa.getNumParcelas(), data)) {
                    totalGastos += festa.getValorParcela();
                }
            }
        }

        return totalGastos;
    }  

    /**
     * Verifica se uma parcela está sendo paga no mês da data especificada.
     *
     * @param dataInicio    Data do primeiro pagamento
     * @param numParcelas   Número total de parcelas
     * @param dataConsulta  Data do mês em que queremos verificar o pagamento
     * @return true se a parcela está sendo paga nesse mês, false caso contrário
     */
    private boolean estaParcelaSendoPaga(LocalDate dataInicio, int numParcelas, LocalDate dataConsulta) {
        if (dataInicio == null || numParcelas <= 0) {
            return false;
        }

        for (int i = 0; i < numParcelas; i++) {
            LocalDate dataParcela = dataInicio.plusMonths(i);
            if (dataParcela.getYear() == dataConsulta.getYear() && dataParcela.getMonthValue() == dataConsulta.getMonthValue()) {
                return true;
            }
        }
        return false;
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

    private LocalDate calcularDataFinal(Casamento casamento, String idPessoa1, String idPessoa2) {
        LocalDate dataFinal = LocalDate.MIN;
    
        // 🔹 Última data considerando parcelas das TAREFAS
        LocalDate ultimaDataTarefa = tarefaRepo.listar().stream()
                .filter(tarefa -> {
                    // Busca o lar da tarefa e verifica se pertence ao casal
                    Lar lar = larRepo.buscarPorId(tarefa.getIdLar());
                    return lar != null
                            && ((lar.getIdPessoa1().equals(idPessoa1) && lar.getIdPessoa2().equals(idPessoa2))
                            || (lar.getIdPessoa1().equals(idPessoa2) && lar.getIdPessoa2().equals(idPessoa1)));
                })
                .map(tarefa -> tarefa.getDataInicio().plusMonths(tarefa.getNumParcelas() - 1)) // Considera parcelas
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);
    
        // 🔹 Última data considerando parcelas das FESTAS
        LocalDate ultimaDataFesta = festaRepo.listar().stream()
                .filter(festa -> festa.getIdCasamento().equals(casamento.getIdCasamento()))
                .map(festa -> festa.getData().plusMonths(festa.getNumParcelas() - 1)) // Considera parcelas
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);
    
        // 🔹 Última data considerando parcelas das COMPRAS
        LocalDate ultimaDataCompra = compraRepo.listar().stream()
                .filter(compra -> {
                    String idTarefa = compra.getIdTarefa();
                    return tarefaRepo.buscarPorId(idTarefa).getIdLar() != null &&
                            larRepo.buscarPorId(tarefaRepo.buscarPorId(idTarefa).getIdLar()).getIdPessoa1().equals(idPessoa1)
                            &&
                            larRepo.buscarPorId(tarefaRepo.buscarPorId(idTarefa).getIdLar()).getIdPessoa2().equals(idPessoa2);
                })
                .map(compra -> {
                    Tarefa tarefa = tarefaRepo.buscarPorId(compra.getIdTarefa());
                    return tarefa.getDataInicio().plusMonths(compra.getNumParcelas() - 1); // Considera parcelas
                })
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);
    
        // 🔹 Determina a maior data entre todas as despesas do casal
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
    