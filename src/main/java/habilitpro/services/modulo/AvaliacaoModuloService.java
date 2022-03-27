package habilitpro.services.modulo;

import habilitpro.model.dao.modulo.AvaliacaoModuloDAO;
import habilitpro.model.persistence.modulo.AvaliacaoModulo;
import habilitpro.model.persistence.modulo.Modulo;
import habilitpro.utils.Validar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
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

        Validar.validarAvaliacao(avaliacaoModulo);

        try {
            beginTransaction();
            avaliacaoModuloDAO.create(avaliacaoModulo);
            commitAndCloseTransaction();
            LOG.info("Avaliação criada com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao criar a avaliação, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        LOG.info("Preparando para encontrar a avaliação.");

        Validar.validarId(id);

        AvaliacaoModulo avaliacaoModulo = getById(id);

        try {
            beginTransaction();
            avaliacaoModuloDAO.delete(avaliacaoModulo);
            commitAndCloseTransaction();
            LOG.info("Avaliação deletada com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao deletar a avaliação, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(AvaliacaoModulo novaAv, Long id) {
        if (novaAv == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        Validar.validarModulo(novaAv.getModulo());
        Validar.validarTrabalhador(novaAv.getTrabalhador());

        AvaliacaoModulo avaliacaoModulo = getById(id);

        try {
            beginTransaction();
            avaliacaoModulo.setNotaAvaliacao(novaAv.getNotaAvaliacao());
            avaliacaoModulo.setAnotacoes(novaAv.getAnotacoes());
            avaliacaoModulo.setModulo(novaAv.getModulo());
            avaliacaoModulo.setTrabalhador(novaAv.getTrabalhador());
            commitAndCloseTransaction();
            LOG.info("Avaliação atualizada com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao atualizar a avaliação, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public AvaliacaoModulo getById(Long id) {
        Validar.validarId(id);

        AvaliacaoModulo avaliacaoModulo = avaliacaoModuloDAO.getById(id);

        Validar.validarAvaliacao(avaliacaoModulo);

        LOG.info("Avaliação encontrada!");
        return avaliacaoModulo;
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

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
