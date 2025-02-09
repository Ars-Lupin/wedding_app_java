package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static List<String[]> lerCSV(String filePath) throws IOException {
        List<String[]> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(";");
                linhas.add(campos);
            }
        } catch (IOException e) {
            throw new IOException("Erro de I/O", e);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IOException("Erro de formatação", e);
        }
        return linhas;
    }
}