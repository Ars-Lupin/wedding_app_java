package model;

/**
 * Classe que representa uma pessoa jurídica no sistema.
 */
public class PessoaJuridica extends Pessoa {
    
    private long cnpj;

    public PessoaJuridica(String nome, String telefone, Endereco endereco, long cnpj) {
        super(nome, telefone, endereco);
        this.cnpj = cnpj; // Verificar possiveis erros depois *******
    }

    public long getCnpj() {
        return cnpj;
    }

    public void setCnpj(long cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * Retorna uma representação textual do objeto. (Para testes de sanidade, por exemplo)
     * 
     * @return String formatada com os dados da pessoa juridica.
     */
    @Override
    public String toString() {
        return "Pessoa Juridica{" +
               "CNPJ='" + cnpj + '\'' +
               '}';
    }
}
