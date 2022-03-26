package habilitpro.services.modulo;

import habilitpro.model.dao.modulo.AvaliacaoModuloDAO;
import habilitpro.model.persistence.modulo.AvaliacaoModulo;
import habilitpro.model.persistence.modulo.Modulo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public class AvaliacaoModuloService {
    private static final Logger LOG = LogManager.getLogger(AvaliacaoModuloService.class);
    private EntityManager entityManager;
    private AvaliacaoModuloDAO avaliacaoModuloDAO;

    public AvaliacaoModuloService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.avaliacaoModuloDAO = new AvaliacaoModuloDAO(entityManager);
    }

    public void create(AvaliacaoModulo avaliacaoModulo) {
        LOG.info("Preparando para criar uma avaliação.");

        validateIfNull(avaliacaoModulo);

        beginTransaction();
        avaliacaoModuloDAO.create(avaliacaoModulo);
        commitAndCloseTransaction();
        LOG.info("Avaliação criada com sucesso!");
    }

    public void delete(Long id) {
        LOG.info("Preparando para encontrar a avaliação.");

        if (id == null) {
            LOG.error("O ID da avaliação está nulo!");
            throw new RuntimeException("ID nulo");
        }

        AvaliacaoModulo avaliacaoModulo = avaliacaoModuloDAO.getById(id);
        validateIfNull(avaliacaoModulo);

        beginTransaction();
        avaliacaoModuloDAO.delete(avaliacaoModulo);
        commitAndCloseTransaction();
        LOG.info("Avaliação deletada com sucesso!");
    }

    public void update(AvaliacaoModulo novaAv, Long id) {
        if (novaAv == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        AvaliacaoModulo avaliacaoModulo = avaliacaoModuloDAO.getById(id);
        validateIfNull(avaliacaoModulo);

        beginTransaction();
        avaliacaoModulo.setNotaAvaliacao(novaAv.getNotaAvaliacao());
        avaliacaoModulo.setAnotacoes(novaAv.getAnotacoes());
        avaliacaoModulo.setModulo(novaAv.getModulo());
        avaliacaoModulo.setTrabalhador(novaAv.getTrabalhador());
        commitAndCloseTransaction();
        LOG.info("Avaliação atualizada com sucesso!");
    }

    public List<AvaliacaoModulo> listAll() {
        LOG.info("Preparando para listar as avaliações.");
        List<AvaliacaoModulo> avaliacoes = avaliacaoModuloDAO.listAll();

        if (avaliacoes == null) {
            LOG.error("Não foram encontradas avaliações!");
            throw new RuntimeException("Não há avaliações");
        }

        LOG.info("Número de módulos encontrados: " + avaliacoes.size());
        return avaliacoes;
    }

    public List<AvaliacaoModulo> listByModulo(Modulo modulo) {
        if (modulo == null) {
            LOG.error("O módulo está nulo!");
            throw new RuntimeException("Módulo nulo");
        }

        LOG.info("Preparando para listar as avaliações do módulo: " + modulo.getNome());
        List<AvaliacaoModulo> avaliacoes = avaliacaoModuloDAO.listByModulo(modulo);

        if (avaliacoes == null) {
            LOG.error("Não foram encontradas avaliações!");
            throw new RuntimeException("Não há avaliações");
        }

        LOG.info("Número de módulos encontrados: " + avaliacoes.size());
        return avaliacoes;
    }

    private void validateIfNull(AvaliacaoModulo avaliacaoModulo) {
        if (avaliacaoModulo == null) {
            this.LOG.error("A avaliação não existe!");
            throw new EntityNotFoundException("Avaliação nula");
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
