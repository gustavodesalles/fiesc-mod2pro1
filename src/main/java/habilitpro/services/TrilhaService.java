package habilitpro.services;

import habilitpro.model.dao.trilha.TrilhaDAO;
import habilitpro.model.persistence.trilha.Trilha;
import javax.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TrilhaService {
    private static final Logger LOG = LogManager.getLogger(TrilhaService.class);
    private EntityManager entityManager;
    private TrilhaDAO trilhaDAO;

    public TrilhaService(EntityManager entityManager, TrilhaDAO trilhaDAO) {
        this.entityManager = entityManager;
        this.trilhaDAO = trilhaDAO;
    }

    public void create(Trilha trilha) {
        LOG.info("Preparando para criar a trilha.");

        validateIfNull(trilha);


    }

    private void validateIfNull(Trilha trilha) {
        if (trilha == null) {
            LOG.error("A trilha não existe!");
            throw new RuntimeException("Trilha nula");
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
