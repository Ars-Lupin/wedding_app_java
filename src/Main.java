import exception.*;

import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.text.ParseException;

import java.util.Scanner;

import repository.CasamentoRepository;
import repository.CompraRepository;
import repository.FestaRepository;
import repository.LarRepository;
import repository.PessoaRepository;
import repository.TarefaRepository;
import repository.CasalRepository;

import service.EstatisticasCasaisService;
import service.EstatisticasPrestadoresService;
import service.PlanejamentoFinanceiro;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String caminhoArquivoEntrada = args[0];

        // Definição dos caminhos dos arquivos CSV
        String caminhoArquivoCasamento = caminhoArquivoEntrada + "/casamentos.csv";
        String caminhoArquivoCompra = caminhoArquivoEntrada + "/compras.csv";
        String caminhoArquivoFesta = caminhoArquivoEntrada + "/festas.csv";
        String caminhoArquivoLares = caminhoArquivoEntrada + "/lares.csv";
        String caminhoArquivoPessoas = caminhoArquivoEntrada + "/pessoas.csv";
        String caminhoArquivoTarefa = caminhoArquivoEntrada + "/tarefas.csv";

        // Inicialização dos repositórios
        CasamentoRepository casamentoRepo = new CasamentoRepository();
        CompraRepository compraRepo = new CompraRepository();
        FestaRepository festaRepo = new FestaRepository();
        LarRepository larRepo = new LarRepository();
        PessoaRepository pessoaRepo = new PessoaRepository();
        TarefaRepository tarefaRepo = new TarefaRepository();
        CasalRepository casalRepo = new CasalRepository();

        try {
            // Carregar dados dos CSVs
            pessoaRepo.carregarDadosDoCSV(caminhoArquivoPessoas);
            larRepo.carregarDados(caminhoArquivoLares, pessoaRepo, casalRepo);
            tarefaRepo.carregarDados(caminhoArquivoTarefa, larRepo, pessoaRepo);
            compraRepo.carregarDados(caminhoArquivoCompra, tarefaRepo, pessoaRepo);
            casamentoRepo.carregarDados(caminhoArquivoCasamento, pessoaRepo, festaRepo, larRepo, casalRepo);
            festaRepo.carregarDados(caminhoArquivoFesta, casamentoRepo, pessoaRepo);
            casamentoRepo.recarregarFestas(festaRepo); // Recarregar festas após carregar os casamentos
            
            String caminhoArquivoRelatorio1 = caminhoArquivoEntrada + "/saida/1-planejamento.csv";
            String caminhoArquivoRelatorio2 = caminhoArquivoEntrada + "/saida/2-estatisticas-prestadores.csv";
            String caminhoArquivoRelatorio3 = caminhoArquivoEntrada + "/saida/3-estatisticas-casais.csv";

            // Gerar estatísticas de casais
            EstatisticasCasaisService estatisticasCasais = new EstatisticasCasaisService(casalRepo, casamentoRepo, pessoaRepo,
                                                                tarefaRepo, festaRepo, compraRepo, larRepo);
            estatisticasCasais.gerarEstatisticas(caminhoArquivoRelatorio3);

            // Gerar estatísticas de prestadores de serviço
            EstatisticasPrestadoresService estatisticasPrestadores = new EstatisticasPrestadoresService(pessoaRepo,
                                                                        tarefaRepo, compraRepo);
            estatisticasPrestadores.gerarRelatorioPrestadores(caminhoArquivoRelatorio2);

            // **Gerar Planejamento Financeiro com CPFs inseridos pelo usuário**
            PlanejamentoFinanceiro planejamento = new PlanejamentoFinanceiro(casalRepo, casamentoRepo, pessoaRepo, tarefaRepo,
                                                        festaRepo, compraRepo, larRepo);

            // Criar ou limpar arquivo CSV
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminhoArquivoRelatorio1),
                    StandardCharsets.UTF_8)) {
            }

            // Ler os CPFs do arquivo de entrada e gerar o planejamento
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();
                
                // Se a linha estiver vazia, encerra o loop (Não há mais CPFs para processar)
                if (linha.isEmpty()) {
                    break;
                }
            
                String[] cpfs = linha.split(",\\s*"); // Divide a linha nos CPFs separados por ", "
                
                if (cpfs.length == 2) {
                    String cpf1 = cpfs[0];
                    String cpf2 = cpfs[1];
            
                    planejamento.gerarPlanejamento(caminhoArquivoRelatorio1, cpf1, cpf2);
                }
            }

        } catch (IOException e) {
            // Qualquer erro de IO é informado ao usuário
            TratamentoExceptions erroDeIO = new TratamentoExceptions(e);
            erroDeIO.EscreveDadosInconsistentesException(caminhoArquivoEntrada);
        } catch (DataInconsistencyException e) {
            // Erros de dados inconsistentes são informados ao usuário
            TratamentoExceptions dadosInconsistentes = new TratamentoExceptions(e);
            dadosInconsistentes.EscreveDadosInconsistentesException(caminhoArquivoEntrada);
        } catch (ParseException e) {
            // Erros de parse são informados ao usuário
            TratamentoExceptions erroDeParse = new TratamentoExceptions(e);
            erroDeParse.EscreveDadosInconsistentesException(caminhoArquivoEntrada);
        }

        scanner.close();
    }
}
