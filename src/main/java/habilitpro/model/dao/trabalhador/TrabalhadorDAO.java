package habilitpro.model.dao.trabalhador;

import habilitpro.model.persistence.trabalhador.Trabalhador;
import javax.persistence.EntityManager;

import java.util.List;

public class TrabalhadorDAO {

    private EntityManager entityManager;

    public TrabalhadorDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Trabalhador trabalhador) {
        entityManager.persist(trabalhador);
    }

    public void delete(Trabalhador trabalhador) {
        entityManager.remove(trabalhador);
    }

    // update será implementado na camada de serviços
    public Trabalhador update(Trabalhador trabalhador) {
        return entityManager.merge(trabalhador);
    }

    public Trabalhador getById(long id) {
        return entityManager.find(Trabalhador.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Trabalhador> listAll() {
        String sql = "SELECT * FROM Trabalhador";
        return entityManager.createNativeQuery(sql, Trabalhador.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Trabalhador> listByFunction(String funcao) {
        String sql = "SELECT * FROM Trabalhador WHERE funcao =: funcao";
        return entityManager.createNativeQuery(sql, Trabalhador.class).setParameter("funcao", funcao).getResultList();
    }
}
