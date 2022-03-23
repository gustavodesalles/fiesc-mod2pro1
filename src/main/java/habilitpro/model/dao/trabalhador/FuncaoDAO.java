package habilitpro.model.dao.trabalhador;

import habilitpro.model.persistence.trabalhador.Funcao;
import habilitpro.model.persistence.trabalhador.Setor;
import javax.persistence.EntityManager;

import java.util.List;

public class FuncaoDAO {
    private EntityManager entityManager;

    public FuncaoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Funcao funcao) {
        entityManager.persist(funcao);
    }

    public void delete(Funcao funcao) {
        entityManager.remove(funcao);
    }

    public Funcao getById(long id) {
        return entityManager.find(Funcao.class, id);
    }

    public Funcao findByName(String name) {
        String sql = "SELECT * FROM Funcao WHERE name =:name";
        return (Funcao) this.entityManager.createNativeQuery(sql, Funcao.class).setParameter("name",name).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Funcao> listBySetor(Setor setor) {
        String sql = "SELECT * FROM Funcao WHERE setor =: setor";
        return entityManager.createNativeQuery(sql, Funcao.class).setParameter("setor", setor).getResultList();
    }
}
