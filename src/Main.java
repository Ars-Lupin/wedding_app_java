import repository.PessoaRepository;
import repository.CasamentoRepository;
import repository.LarRepository;
import repository.TarefaRepository;
import repository.CompraRepository;
import repository.FestaRepository;
import service.EstatisticasCasaisService;
import service.EstatisticasPrestadoresService;

import java.io.IOException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) {
        String caminhoArquivoEntrada = args[0];

        String caminhoArquivoCasamento = caminhoArquivoEntrada + "/casamentos.csv";
        CasamentoRepository casamentoRepo = new CasamentoRepository();

        String caminhoArquivoCompra = caminhoArquivoEntrada + "/compras.csv";
        CompraRepository compraRepo = new CompraRepository();

        String caminhoArquivoFesta = caminhoArquivoEntrada + "/festas.csv";
        FestaRepository festaRepo = new FestaRepository();

        String caminhoArquivoLares = caminhoArquivoEntrada + "/lares.csv";
        LarRepository larRepo = new LarRepository();

        String caminhoArquivoPessoas = caminhoArquivoEntrada + "/pessoas.csv";
        PessoaRepository pessoaRepo = new PessoaRepository();

        String caminhoArquivoTarefa = caminhoArquivoEntrada + "/tarefas.csv";
        TarefaRepository tarefaRepo = new TarefaRepository();

        try {
            // Chama o método do repositório para carregar os dados dos arquivos CSV
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

        /*
         * // Criando o caminho do arquivo final
         * String caminhoArquivo = "planejamento_financeiro.csv";
         * 
         * PlanejamentoFinanceiro f = new PlanejamentoFinanceiro(casamentoRepo,
         * pessoaRepo);
         * List<String> idsCasamentos = casamentoRepo.getIDs();
         * for (String id : idsCasamentos) {
         * Casamento c = casamentoRepo.buscarPorId(id);
         * 
         * Map<String, Double> historicoMensal = new HashMap<>();
         * historicoMensal = f.calcularHistoricoFinanceiro(c);
         * 
         * CSVWriter.escreverCSV(caminhoArquivo, historicoMensal);
         * }
         */

        // Gerar estatísticas
        EstatisticasCasaisService estatisticasCasais = new EstatisticasCasaisService(casamentoRepo, pessoaRepo, tarefaRepo, festaRepo, compraRepo, larRepo);
        estatisticasCasais.gerarEstatisticas("3-estatisticas-casais.csv");

        EstatisticasPrestadoresService estatisticasPrestadores = new EstatisticasPrestadoresService(pessoaRepo, tarefaRepo, compraRepo);
        estatisticasPrestadores.gerarRelatorioPrestadores("2-estatisticas-prestadores.csv");
    }
}
