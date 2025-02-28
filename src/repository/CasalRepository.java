package repository;

import model.Casal;
import model.Casamento;
import model.Lar;

import java.util.*;

import exception.DataInconsistencyException;

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

    /**
     * Adiciona um casal ao repositório.
     * 
     * @param casal
     * @throws DataInconsistencyException Se o casal for nulo
     */
    public void adicionar(Casal casal) throws DataInconsistencyException {

        // Verifica se o casal é nulo
        if (casal == null) {
            throw new DataInconsistencyException("O casal não pode ser nulo (para o repositório).");
        }

        casais.add(casal);
    }


    /**
     * Remove um casal do repositório.
     * 
     * @param casal
     * @throws DataInconsistencyException Se o casal for nulo
     */
    public void remover(Casal casal) throws DataInconsistencyException{

        // Verifica se o casal é nulo
        if (casal == null) {
            throw new DataInconsistencyException("O casal não pode ser nulo (para o repositório).");
        }

        casais.remove(casal);
    }

    /**
     * Retorna a lista de todos os casais cadastrados.
     */
    public List<Casal> listar() {
        return Collections.unmodifiableList(casais);
    }

    /**
     * Carrega os casais a partir dos casamentos e lares.
     * @param casamentoRepo
     * @param larRepo
     */
    public void carregarCasais(CasamentoRepository casamentoRepo, LarRepository larRepo) throws DataInconsistencyException {
        // Iteração sobre os casamentos, verificando quais casais existem e devem ser adicionados ao CasaisRepository
        for (Casamento casamento : casamentoRepo.listar()) {
            Casal casal = casamento.getCasal();

            if (casal != null) {
                adicionar(casal);
            } else {
                throw new DataInconsistencyException("Casal não encontrado para o casamento de ID " + casamento.getIdCasamento());
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
                    throw new DataInconsistencyException("Casal já adicionado ao repositório.");
                }
            } else {
                throw new DataInconsistencyException("Casal não encontrado para o lar de ID " + lar.getIdLar());
            }
        }
    }

    /**
     * Busca um casal pelo ID da pessoa.
     */
    public Casal buscarPorIdPessoa(String idPessoa) {
        for (Casal casal : casais) {
            if (casal.getIdPessoa1().equals(idPessoa) || casal.getIdPessoa2().equals(idPessoa)) {
                return casal;
            }
        }
        return null;
    }

    /**
     * Busca um casal pelo ID do lar.
     */
    public Casal buscarPorIdLar(String idLar) {
        for (Casal casal : casais) {
            if (casal.getIdLar().equals(idLar)) {
                return casal;
            }
        }
        return null;
    }

    /**
     * Busca um casal pelo ID do casamento.
     */
    public Casal buscarPorIdCasamento(String idCasamento) {
        for (Casal casal : casais) {
            if (casal.getIdCasamento().equals(idCasamento)) {
                return casal;
            }
        }
        return null;
    }
}
