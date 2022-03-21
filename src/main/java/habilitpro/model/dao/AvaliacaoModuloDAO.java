package habilitpro.model.dao;

import habilitpro.model.persistence.modulo.AvaliacaoModulo;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AvaliacaoModuloDAO {

    private EntityManager entityManager;

    public AvaliacaoModuloDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(AvaliacaoModulo avaliacaoModulo) {
        entityManager.persist(avaliacaoModulo);
    }

    public void delete(AvaliacaoModulo avaliacaoModulo) {
        entityManager.remove(avaliacaoModulo);
    }

    // update será implementado na camada de serviços

    public AvaliacaoModulo getById(long id) {
        return entityManager.find(AvaliacaoModulo.class, id);
    }

    public List listAll() {
        String sql = "SELECT * FROM AvaliacaoModulo";
        return entityManager.createNativeQuery(sql, AvaliacaoModulo.class).getResultList();
    }

    // A classe "AvaliacaoModulo" foi implementada somente como conveniência para avaliar o
    // desempenho do Trabalhador em cada Módulo, então omite-se o método de listagem com filtro.
}
