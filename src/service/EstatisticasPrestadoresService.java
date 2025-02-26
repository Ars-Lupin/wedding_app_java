package service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import model.Loja;
import model.PessoaFisica;
import model.PessoaJuridica;
import repository.CompraRepository;
import repository.PessoaRepository;
import repository.TarefaRepository;

/**
 * Serviço responsável por gerar as estatísticas dos prestadores.
 */
public class EstatisticasPrestadoresService {

    private final PessoaRepository pessoaRepository;
    private final TarefaRepository tarefaRepository;
    private final CompraRepository compraRepository;

    /**
     * Construtor da classe EstatisticasPrestadores.
     */
    public EstatisticasPrestadoresService(PessoaRepository pessoaRepository, 
                                          TarefaRepository tarefaRepository, 
                                          CompraRepository compraRepository) {
        this.pessoaRepository = pessoaRepository;
        this.tarefaRepository = tarefaRepository;
        this.compraRepository = compraRepository;
    }

    /**
     * Gera o relatório de valores recebidos por prestadores de serviço e lojas.
     */
    public void gerarRelatorioPrestadores(String caminhoSaida) {
        Map<String, Double> valoresRecebidos = new HashMap<>();
        Map<String, String> tiposPrestadores = new HashMap<>();

        // Processa os pagamentos de tarefas (prestadores de serviço)
        tarefaRepository.listar().forEach(tarefa -> {
            String idPrestador = tarefa.getIdPrestador();
            double valor = tarefa.getValorPrestador();
            valoresRecebidos.put(idPrestador, valoresRecebidos.getOrDefault(idPrestador, 0.0) + valor);
        });

        // Processa os pagamentos de compras (lojas)
        compraRepository.listar().forEach(compra -> {
            String idLoja = compra.getIdLoja();
            double valor = compra.valorTotal();
            valoresRecebidos.put(idLoja, valoresRecebidos.getOrDefault(idLoja, 0.0) + valor);
        });

        // Determina os tipos de prestadores
        pessoaRepository.listar().forEach(pessoa -> {
            String id = pessoa.getIdPessoa();
            if (pessoa instanceof PessoaFisica) {
                tiposPrestadores.put(id, "PF");
            } else if (pessoa instanceof Loja) {
                tiposPrestadores.put(id, "Loja");
            } else if (pessoa instanceof PessoaJuridica) {
                tiposPrestadores.put(id, "PJ");
            }

            if (!(pessoa instanceof PessoaFisica)) {
                // Garante que todos os prestadores estejam no mapa, mesmo que com R$ 0,00
                valoresRecebidos.putIfAbsent(id, 0.0);
            }
        });

        // Converte o mapa para uma lista ordenada
        List<Map.Entry<String, Double>> listaOrdenada = new ArrayList<>(valoresRecebidos.entrySet());
        listaOrdenada.sort((e1, e2) -> {
            String tipo1 = tiposPrestadores.get(e1.getKey());
            String tipo2 = tiposPrestadores.get(e2.getKey());
        
            // Mapeia os tipos para valores numéricos
            int peso1 = tipo1.equals("PF") ? 1 : tipo1.equals("PJ") ? 2 : 3;
            int peso2 = tipo2.equals("PF") ? 1 : tipo2.equals("PJ") ? 2 : 3;
        
            // Ordena pelo peso (PF primeiro, depois PJ, depois Loja)
            int comparacaoTipo = Integer.compare(peso1, peso2);
            if (comparacaoTipo != 0) return comparacaoTipo;
        
            // Ordena por valor recebido (decrescente)
            int comparacaoValor = Double.compare(e2.getValue(), e1.getValue());
            if (comparacaoValor != 0) return comparacaoValor;
        
            // Ordena por nome (alfabeticamente)
            String nome1 = pessoaRepository.buscarPorId(e1.getKey()).getNome();
            String nome2 = pessoaRepository.buscarPorId(e2.getKey()).getNome();
            return nome1.compareToIgnoreCase(nome2);
        });

        // Escreve o relatório no arquivo
        try (FileWriter writer = new FileWriter(caminhoSaida, StandardCharsets.UTF_8);
             PrintWriter printer = new PrintWriter(writer)) {
            for (Map.Entry<String, Double> entry : listaOrdenada) {
                String id = entry.getKey();
                String tipo = tiposPrestadores.get(id);
                String nome = pessoaRepository.buscarPorId(id).getNome();
                double totalRecebido = entry.getValue();

                printer.printf("%s;%s;R$ %.2f%n", tipo, nome, totalRecebido);
            }
        } catch (IOException e) { 
            System.err.println("Erro ao gerar relatório de prestadores: " + e.getMessage());
        }
    }
}
