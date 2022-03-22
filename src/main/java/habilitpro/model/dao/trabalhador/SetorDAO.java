package habilitpro.model.dao.trabalhador;

import habilitpro.model.persistence.trabalhador.Setor;
import jakarta.persistence.EntityManager;

public class SetorDAO {
    private EntityManager entityManager;

    public SetorDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Setor setor) {
        entityManager.persist(setor);
    }

    public Setor getById(long id) {
        return entityManager.find(Setor.class, id);
    }

    public Setor findByName(String name) {
        String sql = "SELECT * FROM Setor WHERE name =: name";
        return (Setor) entityManager.createNativeQuery(sql, Setor.class).getSingleResult();
    }
}
