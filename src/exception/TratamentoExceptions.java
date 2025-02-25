package exception;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Exceção que representa dados inconsistentes.
 */
public class TratamentoExceptions {

    private final IllegalArgumentException mensagemDadoInconsistente;
    private final IOException mensagemIO;

    /**
     * Construtor da exceção (mensagemIO).
     * 
     * @param mensagem Mensagem de erro.
     */
    public TratamentoExceptions(IOException mensagemIO) {
        this.mensagemIO = mensagemIO;
        this.mensagemDadoInconsistente = null;
    }

    public TratamentoExceptions(IllegalArgumentException mensagemDadoInconsistente)
    {
        this.mensagemDadoInconsistente = mensagemDadoInconsistente;
        this.mensagemIO = null;
    }
    public void EscreveDadosInconsistentesException(String diretorioBase) {
        // Defina o caminho do diretório 'saida'
        Path diretorioSaida = Paths.get(diretorioBase, "saida");
        
        // Cria o diretório 'saida' caso não exista
        try {
            if (!Files.exists(diretorioSaida)) {
                Files.createDirectories(diretorioSaida);
            }
        } catch (IOException ex) {
            System.err.println("Erro ao criar diretório 'saida': " + ex.getMessage());
            return;
        }

        // Gera os arquivos de planejamento, estatísticas de casais e prestadores vazios
        try (BufferedWriter writer = Files.newBufferedWriter(diretorioSaida.resolve("1-planejamento.csv"), StandardCharsets.UTF_8)) {
            // No content for now, just creating the empty file
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo 1-planejamento.csv: " + ex.getMessage());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(diretorioSaida.resolve("2-estatisticas-prestadores.csv"), StandardCharsets.UTF_8)) {
            // No content for now, just creating the empty file
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo 2-estatisticas-prestadores.csv: " + ex.getMessage());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(diretorioSaida.resolve("3-estatisticas-casais.csv"), StandardCharsets.UTF_8)) {
            // No content for now, just creating the empty file
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo 3-estatisticas-casais.csv: " + ex.getMessage());
        }

        // Exceção é printada no arquivo saida.txt
        try (BufferedWriter writer = Files.newBufferedWriter(diretorioSaida.resolve("saida.txt"), StandardCharsets.UTF_8)) {
            writer.write(this.mensagemDadoInconsistente.getMessage());
            writer.append("\n");
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo saida.txt: " + ex.getMessage());
        }
    }
}
