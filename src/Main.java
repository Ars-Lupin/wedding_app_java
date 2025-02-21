import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
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

        if (args.length > 0) {
            System.out.println(args[0]);
        } else {
            System.out.println("Nenhum argumento foi passado.");
            return;
        }

        String caminhoArquivoEntrada = args[0];

        // Definição dos caminhos dos arquivos
        String caminhoArquivoCasamento = caminhoArquivoEntrada + "/casamentos.csv";
        String caminhoArquivoCompra = caminhoArquivoEntrada + "/compras.csv";
        String caminhoArquivoFesta = caminhoArquivoEntrada + "/festas.csv";
        String caminhoArquivoLares = caminhoArquivoEntrada + "/lares.csv";
        String caminhoArquivoPessoas = caminhoArquivoEntrada + "/pessoas.csv";
        String caminhoArquivoTarefa = caminhoArquivoEntrada + "/tarefas.csv";
        String caminhoArquivoEntradaCPFs = caminhoArquivoEntrada + "/entrada.txt"; // Arquivo com CPFs

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
            larRepo.carregarDados(caminhoArquivoLares);
            casamentoRepo.carregarDados(caminhoArquivoCasamento);
            tarefaRepo.carregarDados(caminhoArquivoTarefa);
            compraRepo.carregarDados(caminhoArquivoCompra);
            festaRepo.carregarDados(caminhoArquivoFesta);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Erro ao converter valores numéricos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao processar dados: " + e.getMessage());
        }


        // Gerar estatísticas de casais
        EstatisticasCasaisService estatisticasCasais = new EstatisticasCasaisService(
                casamentoRepo, pessoaRepo, tarefaRepo, festaRepo, compraRepo, larRepo);
        estatisticasCasais.gerarEstatisticas("3-estatisticas-casais.csv");

        // Gerar estatísticas de prestadores de serviço
        EstatisticasPrestadoresService estatisticasPrestadores = new EstatisticasPrestadoresService(
                pessoaRepo, tarefaRepo, compraRepo);
        estatisticasPrestadores.gerarRelatorioPrestadores("2-estatisticas-prestadores.csv");

        // **Gerar Planejamento Financeiro com os CPFs lidos**
        PlanejamentoFinanceiro planejamento = new PlanejamentoFinanceiro(
                casamentoRepo, pessoaRepo, tarefaRepo, festaRepo, compraRepo);

        System.out.println("Gerando planejamento financeiro...");
        // **Ler CPFs do arquivo `entrada.txt`**
        try {
            List<String> linhas = Files.readAllLines(Paths.get(caminhoArquivoEntradaCPFs));
            for (String linha : linhas) {
                String[] cpfs = linha.split(",\\s*"); // Divide a linha nos CPFs separados por ", "
                if (cpfs.length == 2) {
                    String cpf1 = cpfs[0].trim();
                    String cpf2 = cpfs[1].trim();
                    planejamento.gerarPlanejamento("1-planejamento.csv", cpf1, cpf2);
                } else {
                    System.err.println("Formato inválido de linha em entrada.txt: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler entrada.txt: " + e.getMessage());
        }
    }
}
