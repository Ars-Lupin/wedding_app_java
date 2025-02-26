import exception.TratamentoExceptions;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Scanner;
import repository.CasamentoRepository;
import repository.CompraRepository;
import repository.FestaRepository;
import repository.LarRepository;
import repository.PessoaRepository;
import repository.TarefaRepository;
import service.EstatisticasCasaisService;
import service.EstatisticasPrestadoresService;
import service.PlanejamentoFinanceiro;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        /*
        if (args.length > 0) {
            System.out.println(args[0]);
        } else {
            System.out.println("Nenhum argumento foi passado.");
            return;
        }
        */
        String caminhoArquivoEntrada = args[0];

        // Definição dos caminhos dos arquivos
        String caminhoArquivoCasamento = caminhoArquivoEntrada + "/casamentos.csv";
        String caminhoArquivoCompra = caminhoArquivoEntrada + "/compras.csv";
        String caminhoArquivoFesta = caminhoArquivoEntrada + "/festas.csv";
        String caminhoArquivoLares = caminhoArquivoEntrada + "/lares.csv";
        String caminhoArquivoPessoas = caminhoArquivoEntrada + "/pessoas.csv";
        String caminhoArquivoTarefa = caminhoArquivoEntrada + "/tarefas.csv";

        // Inicialização dos repositórios
        CasamentoRepository casamentoRepo = new CasamentoRepository();
        CompraRepository compraRepo = new CompraRepository();
        FestaRepository festaRepo = new FestaRepository();
        LarRepository larRepo = new LarRepository();
        PessoaRepository pessoaRepo = new PessoaRepository();
        TarefaRepository tarefaRepo = new TarefaRepository();

        try {
            // Carregar dados dos CSVs
            pessoaRepo.carregarDadosDoCSV(caminhoArquivoPessoas);
            larRepo.carregarDados(caminhoArquivoLares, pessoaRepo);
            tarefaRepo.carregarDados(caminhoArquivoTarefa, larRepo, pessoaRepo);
            compraRepo.carregarDados(caminhoArquivoCompra, tarefaRepo, pessoaRepo);
            casamentoRepo.carregarDados(caminhoArquivoCasamento, pessoaRepo, festaRepo);
            festaRepo.carregarDados(caminhoArquivoFesta, casamentoRepo, pessoaRepo);
            casamentoRepo.recarregarFestas(festaRepo);

            String caminhoArquivoRelatorio1 = caminhoArquivoEntrada + "/saida/1-planejamento.csv";
            String caminhoArquivoRelatorio2 = caminhoArquivoEntrada + "/saida/2-estatisticas-prestadores.csv";
            String caminhoArquivoRelatorio3 = caminhoArquivoEntrada + "/saida/3-estatisticas-casais.csv";




            // Gerar estatísticas de casais
            EstatisticasCasaisService estatisticasCasais = new EstatisticasCasaisService(casamentoRepo, pessoaRepo,
                    tarefaRepo, festaRepo, compraRepo, larRepo);
            estatisticasCasais.gerarEstatisticas(caminhoArquivoRelatorio3);

            // Gerar estatísticas de prestadores de serviço
            EstatisticasPrestadoresService estatisticasPrestadores = new EstatisticasPrestadoresService(pessoaRepo,
                    tarefaRepo, compraRepo);
            estatisticasPrestadores.gerarRelatorioPrestadores(caminhoArquivoRelatorio2);

            // **Gerar Planejamento Financeiro com CPFs inseridos pelo usuário**
            PlanejamentoFinanceiro planejamento = new PlanejamentoFinanceiro(casamentoRepo, pessoaRepo, tarefaRepo,
                    festaRepo, compraRepo, larRepo);

            // Criar ou limpar arquivo CSV
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminhoArquivoRelatorio1),
                    StandardCharsets.UTF_8)) {
            }

            while (true) {
                String linha = scanner.nextLine().trim();
                
                if (linha.isEmpty()) {
                    break;
                }

                String[] cpfs = linha.split(",\\s*"); // Divide a linha nos CPFs separados por ", "
                
                if (cpfs.length == 2) {
                    String cpf1 = cpfs[0].trim();
                    String cpf2 = cpfs[1].trim();
                    planejamento.gerarPlanejamento(caminhoArquivoRelatorio1, cpf1, cpf2);
                } else {
                    System.out.println("Formato inválido! Insira exatamente dois CPFs separados por vírgula.");
                }
            }

        } catch (IOException e) {
            TratamentoExceptions erroDeIO = new TratamentoExceptions(e);
            erroDeIO.EscreveDadosInconsistentesException(caminhoArquivoEntrada);
        } catch (IllegalArgumentException e) {
            TratamentoExceptions dadosInconsistentes = new TratamentoExceptions(e);
            dadosInconsistentes.EscreveDadosInconsistentesException(caminhoArquivoEntrada);
        } catch (ParseException e) {
            System.out.println("Erro ao converter valores numéricos: " + e.getMessage());
        }

        scanner.close();
    }
}
