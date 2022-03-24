package habilitpro.services.trilha;

import habilitpro.model.dao.trilha.OcupacaoDAO;
import habilitpro.model.persistence.trilha.Ocupacao;
import habilitpro.model.persistence.trilha.Trilha;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public class OcupacaoService {
    private static final Logger LOG = LogManager.getLogger(OcupacaoService.class);
    private EntityManager entityManager;
    private OcupacaoDAO ocupacaoDAO;

    public OcupacaoService(EntityManager entityManager, OcupacaoDAO ocupacaoDAO) {
        this.entityManager = entityManager;
        this.ocupacaoDAO = ocupacaoDAO;
    }

    //A criação da classe Ocupação já é validada no método create da classe TrilhaService
    // e a sua deleção causaria problemas na tabela de trilhas, então omite-se ambos os métodos.

    public void update(String novoNome, Long id) {
        if (novoNome == null || novoNome.isBlank() || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        LOG.info("Preparando para encontrar a ocupação.");
        Ocupacao ocupacao = ocupacaoDAO.getById(id);
        validateIfNull(ocupacao);
        LOG.info("Ocupação encontrada!");

        beginTransaction();
        ocupacao.setNome(novoNome);
        commitAndCloseTransaction();
    }

    public Ocupacao getById(Long id) {
        if (id == null) {
            LOG.error("O ID da ocupação está nulo!");
            throw new RuntimeException("ID nulo");
        }

        Ocupacao ocupacao = ocupacaoDAO.getById(id);
        validateIfNull(ocupacao);

        LOG.info("Ocupação encontrada!");
        return ocupacao;
    }

    public Ocupacao findByNome(String nome) {
        if (nome == null) {
            LOG.error("O nome da ocupação está nulo!");
            throw new RuntimeException("Nome nulo");
        }

        Ocupacao ocupacao = ocupacaoDAO.findByNome(nome);
        validateIfNull(ocupacao);

        LOG.info("Ocupação encontrada!");
        return ocupacao;
    }

    public List<Ocupacao> listAll() {
        LOG.info("Preparando para listar as ocupações.");
        List<Ocupacao> ocupacoes = ocupacaoDAO.listAll();

        if (ocupacoes == null) {
            LOG.error("Não foram encontradas ocupações!");
            throw new EntityNotFoundException("Não há ocupações");
        }

        LOG.info("Número de ocupações encontradas: " + ocupacoes.size());
        return ocupacoes;
    }

    public List<Ocupacao> listByTrilha(Trilha trilha) {
        if (trilha == null) {
            LOG.error("A trilha está nula!");
            throw new EntityNotFoundException("Trilha nula");
        }

        LOG.info("Preparando para listar as ocupações da trilha: " + trilha.getNome());
        List<Ocupacao> ocupacoes = ocupacaoDAO.listByTrilha(trilha);

        if (ocupacoes == null) {
            LOG.error("Não foram encontradas ocupações da trilha " + trilha.getNome() + "!");
            throw new EntityNotFoundException("Não há ocupações da trilha informada");
        }

        LOG.info("Número de ocupações encontradas: " + ocupacoes.size());
        return ocupacoes;
    }

    private void validateIfNull(Ocupacao ocupacao) {
        if (ocupacao == null) {
            LOG.error("A ocupação não existe!");
            throw new EntityNotFoundException("Ocupação nula");
        }
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
