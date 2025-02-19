package service;

import model.Casamento;
import model.Financeiro;
import model.PessoaFisica;
import repository.CasamentoRepository;
import repository.PessoaRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço responsável pelo cálculo financeiro dos casais.
 */
public class PlanejamentoFinanceiro {

    private final CasamentoRepository casamentoRepository;
    private final PessoaRepository pessoaRepository;
    private static final double RENDIMENTO_POUPANCA = 0.005; // 0.5% ao mês
    private static final int MESES_ANO = 12;

    /**
     * Construtor do FinanceiroService.
     * 
     * @param casamentoRepository Repositório de casamentos.
     * @param pessoaRepository Repositório de pessoas.
     */
    public PlanejamentoFinanceiro(CasamentoRepository casamentoRepository, PessoaRepository pessoaRepository) {
        this.casamentoRepository = casamentoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * Calcula o histórico financeiro mensal de um casal ao longo de um ano.
     * 
     * @param casamento O casamento cujo financeiro será calculado.
     * @return Mapa onde cada mês está associado ao saldo financeiro do casal.
     */
    public Map<String, Double> calcularHistoricoFinanceiro(Casamento casamento) {
        Map<String, Double> historicoMensal = new HashMap<>();

        // Obtém os IDs das pessoas do casamento
        String idPessoa1 = casamento.getIdPessoa1();
        String idPessoa2 = casamento.getIdPessoa2();

        // Busca os dados financeiros das pessoas
        PessoaFisica pessoa1 = (PessoaFisica) pessoaRepository.buscarPorId(idPessoa1);
        PessoaFisica pessoa2 = (PessoaFisica) pessoaRepository.buscarPorId(idPessoa2);

        if (pessoa1 == null || pessoa2 == null) {
            throw new IllegalArgumentException("Não foi possível encontrar os dados de uma das pessoas do casamento.");
        }

        Financeiro financeiro1 = pessoa1.getFinanceiro();
        Financeiro financeiro2 = pessoa2.getFinanceiro();

        // Calcula o saldo inicial do casal
        double saldo = financeiro1.getDinheiroPoupanca() + financeiro2.getDinheiroPoupanca();
        double salarioMensal = financeiro1.getSalarioLiquido() + financeiro2.getSalarioLiquido();
        double gastosMensais = financeiro1.getGastosMensais() + financeiro2.getGastosMensais();

        // Loop para calcular o saldo mês a mês ao longo do ano
        for (int mes = 1; mes <= MESES_ANO; mes++) {
            saldo += salarioMensal - gastosMensais;

            // Em dezembro, adicionamos o 13º salário
            if (mes == 12) {
                saldo += salarioMensal;
            }

            // Aplicação de rendimento da poupança (apenas se o saldo for positivo)
            if (saldo > 0) {
                saldo += saldo * RENDIMENTO_POUPANCA;
                saldo = Math.round(saldo * 100.0) / 100.0; // Arredonda para 2 casas decimais
            }

            // Salva o saldo do mês no histórico
            historicoMensal.put(formatarMes(mes), saldo);
        }

        return historicoMensal;
    }

    /**
     * Formata o nome do mês para o formato "MM/yyyy" considerando o ano atual.
     * 
     * @param mes O número do mês.
     * @return String formatada representando o mês e o ano.
     */
    private String formatarMes(int mes) {
        return String.format("%02d/%d", mes, LocalDate.now().getYear());
    }
}
