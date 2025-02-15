import repository.PessoaRepository;
import repository.CasamentoRepository;
import repository.LarRepository;
import repository.TarefaRepository;
import repository.CompraRepository;
import repository.FestaRepository;

import java.io.IOException;

import java.text.ParseException;

public class Main {

    public static void main(String[] args) {
        String caminhoArquivoPessoas = "Casos/01/pessoas.csv";
        PessoaRepository pessoaRepo = new PessoaRepository();

        String caminhoArquivoLares = "Casos/01/lares.csv";
        LarRepository larRepo = new LarRepository();

        String caminhoArquivoCasamento = "Casos/01/casamentos.csv";
        CasamentoRepository casamentoRepo  = new CasamentoRepository();

        String caminhoArquivoTarefa = "Casos/01/tarefas.csv";
        TarefaRepository tarefaRepo = new TarefaRepository();

        String caminhoArquivoCompra = "Casos/01/compras.csv";
        CompraRepository compraRepo = new CompraRepository();

        String caminhoArquivoFesta = "Casos/01/festas.csv";
        FestaRepository festaRepo = new FestaRepository();

        try {
            // Chama o método do repositório para carregar os dados dos arquivos CSV
            pessoaRepo.carregarDadosDoCSV(caminhoArquivoPessoas);
            larRepo.carregarDados(caminhoArquivoLares);
            casamentoRepo.carregarDados(caminhoArquivoCasamento);
            tarefaRepo.carregarDados(caminhoArquivoTarefa);
            compraRepo.carregarDados(caminhoArquivoCompra);
            festaRepo.carregarDados(caminhoArquivoFesta);

            System.out.println("\nFestas carregadas no repositório:");
            festaRepo.listar().forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Erro ao converter valores numéricos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao processar dados: " + e.getMessage());
        }
    }
}
