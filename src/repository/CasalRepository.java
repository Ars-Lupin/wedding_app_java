package repository;

import model.Casal;
import model.Casamento;
import model.Lar;

import java.util.*;

/**
 * Repositório responsável por armazenar e gerenciar os casais.
 */
public class CasalRepository {
    
    // Lista dos casais (list)
    private final List<Casal> casais;

    /**
     * Construtor do repositório de casais.
     */
    public CasalRepository() {
        this.casais = new ArrayList<>();
    }

    public void adicionar(Casal casal) {

        // Verifica se o casal é nulo
        if (casal == null) {
            throw new IllegalArgumentException("O casal não pode ser nulo (para o repositório).");
        }

        casais.add(casal);
    }

    public void remover(Casal casal) {

        // Verifica se o casal é nulo
        if (casal == null) {
            throw new IllegalArgumentException("O casal não pode ser nulo (para o repositório).");
        }

        casais.remove(casal);
    }

    /**
     * Retorna a lista de todos os casais cadastrados.
     */
    public List<Casal> listar() {
        return Collections.unmodifiableList(casais);
    }

    public void carregarCasais(CasamentoRepository casamentoRepo, LarRepository larRepo)
    {
        // Iteração sobre os casamentos, verificando quais casais existem e devem ser adicionados ao CasaisRepository
        for (Casamento casamento : casamentoRepo.listar()) {
            Casal casal = casamento.getCasal();

            if (casal != null) {
                adicionar(casal);
            } else {
                throw new IllegalArgumentException("Casal não encontrado para o casamento de ID " + casamento.getIdCasamento());
            }
        }

        // Iteração sobre os lares, verificando quais casais existem e devem ser adicionados ao CasaisRepository
        for (Lar lar : larRepo.listar()) {
            Casal casal = lar.getCasal();

            // Adicionar apenas se o casal já não está no repositório
            if (casal != null) {
                if (!casais.contains(casal)) {
                    adicionar(casal);
                } else {
                    throw new IllegalArgumentException("Casal já adicionado ao repositório.");
                }
            } else {
                throw new IllegalArgumentException("Casal não encontrado para o lar de ID " + lar.getIdLar());
            }
        }
    }

    /**
     * Busca um casal pelo ID de uma das pessoas envolvidas.
     */
    public Casal buscarPorIdPessoa(String idPessoa) {
        for (Casal casal : casais) {
            if (casal.getIdPessoa1().equals(idPessoa) || casal.getIdPessoa2().equals(idPessoa)) {
                return casal;
            }
        }
        return null;
    }

    public Casal buscarPorIdLar(String idLar) {
        for (Casal casal : casais) {
            if (casal.getIdLar().equals(idLar)) {
                return casal;
            }
        }
        return null;
    }

    public Casal buscarPorIdCasamento(String idCasamento) {
        for (Casal casal : casais) {
            if (casal.getIdCasamento().equals(idCasamento)) {
                return casal;
            }
        }
        return null;
    }
}
