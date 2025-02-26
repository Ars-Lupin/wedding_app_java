package util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CSVWriter {

    /**
     * Escreve um arquivo CSV a partir de uma lista de linhas.
     *
     * @param filePath Caminho do arquivo CSV.
     * @param linhas   Lista de linhas a serem escritas (cada linha é uma lista de colunas).
     * @param append   Se true, adiciona ao arquivo; se false, sobrescreve o arquivo.
     */
    public static void escreverCSV(String filePath, List<List<String>> linhas, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, append), StandardCharsets.UTF_8))) {

            for (List<String> linha : linhas) {
                String linhaCSV = String.join("", linha);
                writer.append(linhaCSV).append("\n");
            }

        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo CSV: " + e.getMessage());
        }
    }
}
