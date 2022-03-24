package habilitpro.model.dao.modulo;

import habilitpro.model.persistence.modulo.AvaliacaoModulo;
import habilitpro.model.persistence.modulo.Modulo;

import javax.persistence.EntityManager;

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

    @SuppressWarnings("unchecked")
    public List<AvaliacaoModulo> listAll() {
        String sql = "SELECT * FROM AvaliacaoModulo";
        return entityManager.createNativeQuery(sql, AvaliacaoModulo.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<AvaliacaoModulo> listByModulo(Modulo modulo) {
        String sql = "SELECT * FROM AvaliacaoModulo where modulo_id = " + modulo.getId();
        return entityManager.createNativeQuery(sql, AvaliacaoModulo.class).setParameter("modulo_id", modulo.getId()).getResultList();
    }

}
