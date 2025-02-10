import repository.PessoaRepository;
import repository.LarRepository;
import repository.TarefaRepository;
import repository.CasamentoRepository;
import repository.FestaRepository;
import repository.CompraRepository;
import util.CSVReader;
import model.Pessoa;
import model.PessoaFisica;
import model.PessoaJuridica;
import model.Lar;
import model.Tarefa;
import model.Casamento;
import model.Festa;
import model.Compra;
import model.Endereco;
import model.Financeiro;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PessoaRepository pessoaRepository = new PessoaRepository();

        try {
            // Carregar dados de pessoas
            List<String[]> pessoas = CSVReader.lerCSV("");
            for (String[] campos : pessoas) {
                String id = campos[0];
                String tipo = campos[1];
                if (tipo.equals("F")) {
                    LocalDate dataNascimento = LocalDate.parse(campos[6]);
                    Financeiro financeiroPF = new Financeiro(Double.parseDouble(campos[7]), Double.parseDouble(campos[8]), 
                                                             Double.parseDouble(campos[9]));
                    PessoaFisica pessoaFisica = new PessoaFisica(campos[2], campos[3], campos[4], 
                                                                 campos[5], dataNascimento, financeiroPF, id);
                    pessoaRepository.adicionar(pessoaFisica);
                } else if (tipo.equals("J")) {
                    PessoaJuridica pessoaJuridica = new PessoaJuridica(campos[2], campos[3], 
                                                                       campos[4], campos[5], id);
                    pessoaRepository.adicionar(pessoaJuridica);
                } else if (tipo.equals("L")) {
                    PessoaJuridica loja = new PessoaJuridica(
                        campos[2], campos[3], campos[4], campos[5], id);
                    pessoaRepository.adicionar(loja);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
