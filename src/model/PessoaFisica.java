package model;
import java.util.Date;

/**
 * Classe que representa uma pessoa física, herdando características
 * de Pessoa
 */
public class PessoaFisica extends Pessoa{

    private long cpf;
    private Date dataNascimento;
    private Financeiro financeiro;

    public PessoaFisica(String nome, String telefone, Endereco endereco, long cpf, 
                        Date dataNascimento, Financeiro financeiro) {
        super(nome, telefone, endereco);
        this.cpf = cpf; // Verificar se o CPF é válido depois ******
        this.dataNascimento = dataNascimento; // Verificar se o data é válida depois ******
        this.financeiro = financeiro;
    }

    public long getCpf() {
        return cpf;
    }

    public void setCpf(long cpf) {
        this.cpf = cpf;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Financeiro getFinanceiro() {
        return financeiro;
    }

    public void setFinanceiro(Financeiro financeiro) {
        this.financeiro = financeiro;
    }

    /**
     * Retorna uma representação textual do objeto. (Para testes de sanidade, por exemplo)
     * 
     * @return String formatada com os dados da pessoa.
     */
    @Override
    public String toString() {
        return "Pessoa Fisica{" +
               "CPF='" + cpf + '\'' +
               '}';
    }
}
