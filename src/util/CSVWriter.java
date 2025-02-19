package util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Classe utilitária para escrever arquivos CSV.
 */
public class CSVWriter {

    /**
     * Escreve um relatório financeiro em formato CSV.
     *
     * @param filePath       Caminho do arquivo CSV.
     * @param historicoMensal Dados financeiros a serem gravados.
     */
    public static void escreverCSV(String filePath, Map<String, Double> historicoMensal) {
        try (FileWriter writer = new FileWriter(filePath)) {

            for (Map.Entry<String, Double> entry : historicoMensal.entrySet()) {
                writer.append(entry.getKey()).append(",")
                      .append(String.format("%.2f", entry.getValue()))
                      .append("\n");
            }

            System.out.println("Relatório financeiro salvo em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao escrever o arquivo CSV: " + e.getMessage());
        }
    }
}
