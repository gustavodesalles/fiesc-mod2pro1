package habilitpro.model.dao.trabalhador;

import habilitpro.model.persistence.trabalhador.Funcao;
import jakarta.persistence.EntityManager;

public class FuncaoDAO {
    private EntityManager entityManager;

    public FuncaoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Funcao funcao) {
        entityManager.persist(funcao);
    }

    public Funcao getById(long id) {
        return entityManager.find(Funcao.class, id);
    }

    public Funcao findByName(String name) {
        String sql = "SELECT * FROM Funcao WHERE name =: name";
        return (Funcao) entityManager.createNativeQuery(sql, Funcao.class).getSingleResult();
    }
}
