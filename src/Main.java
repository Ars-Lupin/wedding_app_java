import model.PessoaFisica;
import model.Financeiro;
import repository.PessoaRepository;
import util.CSVReader;

import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        String caminhoArquivo = "Casos/01/pessoas.csv";
        PessoaRepository pessoaRepo = new PessoaRepository();

        try {
            List<String[]> linhas = CSVReader.lerCSV(caminhoArquivo);
            System.out.println("Arquivo lido com sucesso! Total de linhas: " + linhas.size());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Formato de data esperado: dd/MM/yyyy
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR")); // Define formato brasileiro

            for (String[] campos : linhas) {
                if (campos.length < 10) { // Verifica se há campos suficientes
                    System.err.println("Linha inválida encontrada, ignorando: " + String.join(";", campos));
                    continue;
                }

                String id = campos[0].trim();
                String nome = campos[2].trim();
                String telefone = campos[3].trim();
                String endereco = campos[4].trim();
                String cpf = campos[5].trim();
                LocalDate dataNasc = LocalDate.parse(campos[6].trim(), formatter); // Converte para LocalDate

                // Converte valores financeiros corretamente usando NumberFormat
                double dinheiroPoupanca = numberFormat.parse(campos[7].trim()).doubleValue();
                double salarioLiquido = numberFormat.parse(campos[8].trim()).doubleValue();
                double gastosMensais = numberFormat.parse(campos[9].trim()).doubleValue();

                // Criando um objeto Pessoa
                Financeiro financeiro = new Financeiro(dinheiroPoupanca, salarioLiquido, gastosMensais);
                PessoaFisica pessoaFisica = new PessoaFisica(nome, telefone, endereco, 
                                                             cpf, dataNasc, financeiro, id);
                pessoaRepo.adicionar(pessoaFisica);
            }

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
