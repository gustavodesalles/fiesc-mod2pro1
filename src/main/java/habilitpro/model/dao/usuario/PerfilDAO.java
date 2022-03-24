package habilitpro.model.dao.usuario;

import habilitpro.model.persistence.usuario.Perfil;
import javax.persistence.EntityManager;
import java.util.List;

public class PerfilDAO {
    private EntityManager entityManager;

    public PerfilDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Perfil perfil) {
        entityManager.persist(perfil);
    }

    public void delete(Perfil perfil) {
        entityManager.remove(perfil);
    }

    public Perfil update(Perfil perfil) {
        return entityManager.merge(perfil);
    }

    public Perfil getById(long id) {
        return entityManager.find(Perfil.class, id);
    }

    public Perfil findByNome(String nome) {
        String sql = "SELECT * FROM Perfil WHERE nome =: nome";
        return (Perfil) entityManager.createNativeQuery(sql, Perfil.class).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Perfil> listAll() {
        String sql = "SELECT * FROM Perfil";
        return entityManager.createNativeQuery(sql, Perfil.class).getResultList();
    }

    //Como haverá somente três perfis, não há muita utilidade em implementar
    // a listagem com filtro, então esta será omissa.
}
