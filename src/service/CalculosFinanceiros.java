package service;

import model.Casamento;
import model.Financeiro;
import model.PessoaFisica;
import repository.CasamentoRepository;
import repository.PessoaRepository;
import repository.TarefaRepository;
import repository.CompraRepository;
import repository.FestaRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Serviço para cálculos financeiros relacionados a casais e seus planejamentos.
 */
public class CalculosFinanceiros {

    private static final int MESES_ANO = 12;
    private static final double RENDIMENTO_POUPANCA = 0.005; // Rendimento mensal de 0.5%
    
    private final CasamentoRepository casamentoRepository;
    private final PessoaRepository pessoaRepository;
    private final TarefaRepository tarefaRepository;
    private final CompraRepository compraRepository;
    private final FestaRepository festaRepository;

    /**
     * Construtor do serviço financeiro.
     *
     * @param casamentoRepository Repositório de casamentos.
     * @param pessoaRepository    Repositório de pessoas.
     * @param tarefaRepository    Repositório de tarefas.
     * @param compraRepository    Repositório de compras.
     * @param festaRepository     Repositório de festas.
     */
    public CalculosFinanceiros(
            CasamentoRepository casamentoRepository,
            PessoaRepository pessoaRepository,
            TarefaRepository tarefaRepository,
            CompraRepository compraRepository,
            FestaRepository festaRepository) {
        this.casamentoRepository = casamentoRepository;
        this.pessoaRepository = pessoaRepository;
        this.tarefaRepository = tarefaRepository;
        this.compraRepository = compraRepository;
        this.festaRepository = festaRepository;
    }

    /**
     * Calcula o histórico financeiro mensal de um casal ao longo de um ano.
     *
     * @param casamento O casamento cujo financeiro será calculado.
     * @return Mapa onde cada mês está associado ao saldo financeiro do casal.
     */
    public Map<String, Double> calcularHistoricoFinanceiro(Casamento casamento) {
        Map<String, Double> historicoMensal = new HashMap<>();

        // Obtendo IDs das pessoas no casamento
        String idPessoa1 = casamento.getIdPessoa1();
        String idPessoa2 = casamento.getIdPessoa2();

        // Buscando os dados financeiros das pessoas no repositório
        PessoaFisica pessoa1 = (PessoaFisica) pessoaRepository.buscarPorId(idPessoa1);
        PessoaFisica pessoa2 = (PessoaFisica) pessoaRepository.buscarPorId(idPessoa2);

        if (pessoa1 == null || pessoa2 == null) {
            throw new IllegalArgumentException("Não foi possível encontrar os dados financeiros de uma das pessoas.");
        }

        Financeiro financeiro1 = pessoa1.getFinanceiro();
        Financeiro financeiro2 = pessoa2.getFinanceiro();

        // Inicializando valores financeiros do casal
        double saldo = financeiro1.getDinheiroPoupanca() + financeiro2.getDinheiroPoupanca();
        double salarioMensal = financeiro1.getSalarioLiquido() + financeiro2.getSalarioLiquido();
        double gastosMensais = financeiro1.getGastosMensais() + financeiro2.getGastosMensais();

        // Loop para calcular o saldo mês a mês ao longo de um ano
        for (int mes = 1; mes <= MESES_ANO; mes++) {
            saldo += salarioMensal - gastosMensais;

            // Adiciona o 13º salário em dezembro
            if (mes == 12) {
                saldo += salarioMensal;
            }

            // Aplicação de rendimento da poupança se o saldo for positivo
            if (saldo > 0) {
                saldo += saldo * RENDIMENTO_POUPANCA;
                saldo = Math.round(saldo * 100.0) / 100.0; // Arredonda para 2 casas decimais
            }

            // Adiciona ao histórico financeiro
            historicoMensal.put(formatarMes(mes), saldo);
        }

        return historicoMensal;
    }

    /**
     * Formata o nome do mês para "MM/yyyy" considerando o ano atual.
     *
     * @param mes O número do mês.
     * @return String representando o mês e o ano.
     */
    private String formatarMes(int mes) {
        return String.format("%02d/%d", mes, java.time.LocalDate.now().getYear());
    }
}
