package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe utilitária para leitura de arquivos CSV.
 */
public class CSVReader {

    private static final Pattern SEPARADOR = Pattern.compile(";");

    /**
     * Lê um arquivo CSV e retorna uma lista de arrays de strings com os campos de cada linha.
     * 
     * @param filePath Caminho do arquivo CSV.
     * @return Lista de linhas, onde cada linha é um array de campos.
     * @throws IOException Se houver erro ao abrir ou ler o arquivo.
     */
    public static List<String[]> lerCSV(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("O caminho do arquivo não pode ser nulo ou vazio.");
        }

        List<String[]> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = SEPARADOR.split(linha); // Usa um Pattern para dividir
                linhas.add(campos);
            }
        }
        return linhas;
    }
}
