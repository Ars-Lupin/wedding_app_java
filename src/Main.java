import repository.PessoaRepository;
import java.io.IOException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) {
        String caminhoArquivo = "Casos/01/pessoas.csv";
        PessoaRepository pessoaRepo = new PessoaRepository();

        try {
            // Chama o método do repositório para carregar os dados do CSV
            pessoaRepo.carregarDadosDoCSV(caminhoArquivo);

            // Exibir as pessoas carregadas
            System.out.println("\nPessoas carregadas no repositório:");
            pessoaRepo.listar().forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Erro ao converter valores numéricos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao processar dados: " + e.getMessage());
        }
    }
}
