package habilitpro.model.dao.usuario;

import habilitpro.model.persistence.usuario.Usuario;
import javax.persistence.EntityManager;

import java.util.List;

public class UsuarioDAO {
    private EntityManager entityManager;

    public UsuarioDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Usuario usuario) {
        entityManager.persist(usuario);
    }

    public void delete(Usuario usuario) {
        entityManager.remove(usuario);
    }

    public Usuario update(Usuario usuario) {
        return entityManager.merge(usuario);
    }

    public Usuario getById(long id) {
        return entityManager.find(Usuario.class, id);
    }

    public Usuario getByEmailAndSenha(String email, String senha) {
        String sql = "SELECT * FROM Usuario WHERE email = ?1 AND senha = ?2";
        return (Usuario) entityManager.createNativeQuery(sql, Usuario.class).setParameter(1, email).setParameter(2, senha).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Usuario> listAll() {
        String sql = "SELECT * FROM Usuario";
        return entityManager.createNativeQuery(sql, Usuario.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Usuario> listByNome(String nome) {
        String sql = "SELECT * FROM Usuario WHERE nome =: nome";
        return entityManager.createNativeQuery(sql, Usuario.class).setParameter("nome", nome).getResultList();
    }
}
