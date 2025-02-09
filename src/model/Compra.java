package model;

/**
 * Classe que representa uma compra de materiais associada à organização de uma tarefa.
 */
public class Compra {
        
        private final String idCompra;
        private final String idLoja;
        private final String idTarefa;
        private String nomeProduto;
        private int quantidade;
        private double valorUnitario;
        private int numParcelas;
    
        /**
        * Construtor da classe Compra.
        *
        * @param idCompra      Identificador único da compra (32 dígitos).
        * @param idLoja        Identificador da loja.
        * @param idTarefa      Identificador da tarefa associada.
        * @param nomeProduto   Nome do produto comprado.
        * @param quantidade    Quantidade de itens comprados.
        * @param valorUnitario Valor unitário dos itens comprados.
        * @param numParcelas   Número de parcelas do pagamento.
        */
        public Compra(String idCompra, String idLoja, String idTarefa, String nomeProduto, 
                        int quantidade, double valorUnitario, int numParcelas) {
            
            if (idCompra == null || !idCompra.matches("\\d{32}")) {
                throw new IllegalArgumentException("O ID da compra deve ter exatamente 32 dígitos numéricos.");
            }
            if (idLoja == null || !idLoja.matches("\\d{32}")) {
                throw new IllegalArgumentException("O ID da loja deve ter exatamente 32 dígitos numéricos.");
            }
            if (idTarefa == null || !idTarefa.matches("\\d{32}")) {
                throw new IllegalArgumentException("O ID da tarefa deve ter exatamente 32 dígitos numéricos.");
            }
            if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
                throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
            }
            if (quantidade <= 0) {
                throw new IllegalArgumentException("A quantidade de itens comprados deve ser maior que zero.");
            }
            if (valorUnitario <= 0) {
                throw new IllegalArgumentException("O valor unitário dos itens comprados deve ser maior que zero.");
            }  
            if (numParcelas <= 0) {
                throw new IllegalArgumentException("O número de parcelas deve ser maior que zero.");
            }
    
            this.idCompra = idCompra;
            this.idLoja = idLoja;
            this.idTarefa = idTarefa;
            this.nomeProduto = nomeProduto;
            this.quantidade = quantidade;
            this.valorUnitario = valorUnitario;
        }

        public String getIdCompra() {
            return idCompra;
        }

        public String getIdLoja() {
            return idLoja;
        }

        public String getIdTarefa() {
            return idTarefa;
        }

        public String getNomeProduto() {
            return nomeProduto;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public double getValorUnitario() {
            return valorUnitario;
        }

        public int getNumParcelas() {
            return numParcelas;
        }
    
        /**
         * Retorna uma representação textual da compra.
         */
        @Override
        public String toString() {
            return String.format("Compra{ID='%s', ID Loja='%s', ID Tarefa='%s', Produto='%s', Quantidade=%d, Valor Unitário=%.2f, Parcelas=%d}",
                                 idCompra, idLoja, idTarefa, nomeProduto, quantidade, valorUnitario, numParcelas);
        }
}
