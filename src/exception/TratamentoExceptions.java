package exception;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
        // Caminhos dos arquivos com base no diretório recebido
        String caminhoArquivoRelatorio1 = diretorioBase + "/saida/1-planejamento.csv";
        String caminhoArquivoRelatorio2 = diretorioBase + "/saida/2-estatisticas-prestadores.csv";
        String caminhoArquivoRelatorio3 = diretorioBase + "/saida/3-estatisticas-casais.csv";
        String caminhoArquivoSaidaTxt = diretorioBase + "/saida/saida.txt";
    
        // Cria o diretório 'saida' caso não exista
        Path diretorioSaida = Paths.get(diretorioBase, "saida");
        try {
            if (!Files.exists(diretorioSaida)) {
                Files.createDirectories(diretorioSaida); // Criação do diretório 'saida'
            }
        } catch (IOException ex) {
            System.err.println("Erro ao criar diretório 'saida': " + ex.getMessage());
            return; // Se não puder criar o diretório, termina a execução
        }
    
        // Gera os arquivos de planejamento, estatísticas de casais e prestadores vazios
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminhoArquivoRelatorio1), StandardCharsets.UTF_8)) {
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo 1-planejamento.csv: " + ex.getMessage());
        }
    
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminhoArquivoRelatorio2), StandardCharsets.UTF_8)) {
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo 2-estatisticas-prestadores.csv: " + ex.getMessage());
        }
    
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminhoArquivoRelatorio3), StandardCharsets.UTF_8)) {
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo 3-estatisticas-casais.csv: " + ex.getMessage());
        }
    
        // Exceção é printada no arquivo saida.txt
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminhoArquivoSaidaTxt), StandardCharsets.UTF_8)) {
            writer.write(this.mensagemDadoInconsistente.getMessage());
            if(this.mensagemDadoInconsistente.getMessage()!= null){
            writer.append("\n");}
            writer.close();
        } catch (IOException ex) {
            System.err.println("Erro ao escrever no arquivo saida.txt: " + ex.getMessage());
        }
    }
    
}
