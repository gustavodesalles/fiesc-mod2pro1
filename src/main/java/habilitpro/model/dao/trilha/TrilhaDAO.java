package habilitpro.model.dao.trilha;

import habilitpro.model.persistence.empresa.Empresa;
import habilitpro.model.persistence.modulo.Modulo;
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

    public boolean checkIfModulo(Trilha trilha) {
        String sql = "SELECT * FROM Modulo WHERE trilha_id =:trilha_id";
        return (entityManager.createNativeQuery(sql, Modulo.class).setParameter("trilha_id", trilha.getId()).getResultList().size() > 0);
    }

    @SuppressWarnings("unchecked")
    public List<Trilha> listAll() {
        String sql = "SELECT * FROM Trilha";
        return entityManager.createNativeQuery(sql, Trilha.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Trilha> listByEmpresa(Empresa empresa) {
        String sql = "SELECT * FROM Trilha WHERE empresa_id =:empresa_id";
        return entityManager.createNativeQuery(sql, Trilha.class).setParameter("empresa_id", empresa.getId()).getResultList();
    }
}
