package habilitpro.model.dao;

import habilitpro.model.persistence.trilha.Ocupacao;
import jakarta.persistence.EntityManager;

public class OcupacaoDAO {
    private EntityManager entityManager;

    public OcupacaoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Ocupacao ocupacao) {
        entityManager.persist(ocupacao);
    }

    public Ocupacao getById(long id) {
        return entityManager.find(Ocupacao.class, id);
    }

    public Ocupacao findByName(String name) {
        String sql = "SELECT * FROM Ocupacao WHERE name =: name";
        return (Ocupacao) entityManager.createNativeQuery(sql, Ocupacao.class).getSingleResult();
    }
}
