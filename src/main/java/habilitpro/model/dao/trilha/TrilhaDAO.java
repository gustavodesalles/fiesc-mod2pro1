package habilitpro.model.dao.trilha;

import habilitpro.model.persistence.trilha.Trilha;
import javax.persistence.EntityManager;

import java.util.List;

public class TrilhaDAO {

    private EntityManager entityManager;

    public TrilhaDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Trilha trilha) {
        entityManager.persist(trilha);
    }

    public void delete(Trilha trilha) {
        entityManager.remove(trilha);
    }

    // update será implementado na camada de serviços

    public Trilha getById(long id) {
        return entityManager.find(Trilha.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Trilha> listAll() {
        String sql = "SELECT * FROM Trilha";
        return entityManager.createNativeQuery(sql, Trilha.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Trilha> listByName(String name) {
        String sql = "SELECT * FROM Trilha WHERE name =: name";
        return entityManager.createNativeQuery(sql, Trilha.class).setParameter("name", name).getResultList();
    }
}
