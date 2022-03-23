package habilitpro.model.dao.trabalhador;

import habilitpro.model.persistence.Empresa;
import habilitpro.model.persistence.trabalhador.Setor;
import javax.persistence.EntityManager;

import java.util.List;

public class SetorDAO {
    private EntityManager entityManager;

    public SetorDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Setor setor) {
        entityManager.persist(setor);
    }

    public void delete(Setor setor) {
        entityManager.remove(setor);
    }

    public Setor getById(long id) {
        return entityManager.find(Setor.class, id);
    }

    public Setor findByName(String name) {
        String sql = "SELECT * FROM Setor WHERE name =:name";
        return (Setor) this.entityManager.createNativeQuery(sql, Setor.class).setParameter("name",name).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Setor> listByEmpresa(Empresa empresa) {
        String sql = "SELECT * FROM Setor WHERE empresa =: empresa";
        return entityManager.createNativeQuery(sql, Setor.class).setParameter("empresa", empresa).getResultList();
    }
}
