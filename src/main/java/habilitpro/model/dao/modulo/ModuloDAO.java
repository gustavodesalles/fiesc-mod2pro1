package habilitpro.model.dao.modulo;

import habilitpro.model.enums.EnumStatusModulo;
import habilitpro.model.persistence.modulo.Modulo;
import javax.persistence.EntityManager;

import java.util.List;

public class ModuloDAO {

    private EntityManager entityManager;

    public ModuloDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Modulo modulo) {
        entityManager.persist(modulo);
    }

    public void delete(Modulo modulo) {
        entityManager.remove(modulo);
    }

    // update será implementado na camada de serviços

    public Modulo getById(long id) {
        return entityManager.find(Modulo.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Modulo> listAll() {
        String sql = "SELECT * FROM modulo";
        return entityManager.createNativeQuery(sql, Modulo.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Modulo> listByStatus(EnumStatusModulo status) {
        String sql = "SELECT * FROM modulo WHERE status =: status";
        return entityManager.createNativeQuery(sql, Modulo.class).setParameter("status", status).getResultList();
    }
}
