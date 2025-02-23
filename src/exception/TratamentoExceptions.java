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

    public void EscreveDadosInconsistentesException() {

        // Gera os arquivos de planejamento, estatisticas de casais e prestadores vazios
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get("1-planejamento.csv"), StandardCharsets.UTF_8)) {
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo 1-planejamento-financeiro.csv: " + ex.getMessage());
        }

        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get("2-estatisticas-prestadores.csv"), StandardCharsets.UTF_8)) {
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo 2-estatisticas-prestadores.csv: " + ex.getMessage());
        }

        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get("3-estatisticas-casais.csv"), StandardCharsets.UTF_8)) {
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo 3-estatisticas-casais.csv: " + ex.getMessage());
        }

        // Exceção é printada no arquivo saida.txt
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get("saida.txt"), StandardCharsets.UTF_8)) {
            writer.write(this.mensagemDadoInconsistente.getMessage());
            writer.append("\n");
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo saida.txt: " + ex.getMessage());
        }
        return;
    }
}
