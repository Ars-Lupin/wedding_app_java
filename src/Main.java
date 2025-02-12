import repository.PessoaRepository;
import repository.CasamentoRepository;
import repository.LarRepository;

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

        try {
            // Chama o método do repositório para carregar os dados dos arquivos CSV
            pessoaRepo.carregarDadosDoCSV(caminhoArquivoPessoas);
            larRepo.carregarDados(caminhoArquivoLares);
            casamentoRepo.carregarDados(caminhoArquivoCasamento);

            System.out.println("\nLares carregados no repositório:");
            casamentoRepo.listar().forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Erro ao converter valores numéricos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao processar dados: " + e.getMessage());
        }
    }
}
