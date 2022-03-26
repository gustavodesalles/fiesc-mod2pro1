package habilitpro.model.dao.empresa;

import habilitpro.model.persistence.empresa.Empresa;
import habilitpro.model.persistence.empresa.Setor;
import habilitpro.model.persistence.trabalhador.Funcao;

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

    public Setor update(Setor setor) {
        return entityManager.merge(setor);
    }

    public Setor getById(long id) {
        return entityManager.find(Setor.class, id);
    }

    public Setor findByName(String nome) {
        String sql = "SELECT * FROM Setor WHERE nome =:nome";
        return (Setor) this.entityManager.createNativeQuery(sql, Setor.class).setParameter("nome", nome).getSingleResult();
    }

    public boolean checkIfFuncao(Setor setor) {
        String sql = "SELECT * FROM Funcao WHERE setor_id =:setor_id";
        return (entityManager.createNativeQuery(sql, Funcao.class).setParameter("setor_id", setor.getId()).getResultList().size() > 0);
    }

    @SuppressWarnings("unchecked")
    public List<Setor> listAll() {
        String sql = "SELECT * FROM Setor";
        return entityManager.createNativeQuery(sql, Setor.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Setor> listByEmpresa(Empresa empresa) {
        String sql = "SELECT * FROM Setor WHERE empresa_id =:empresa_id";
        return entityManager.createNativeQuery(sql, Setor.class).setParameter("empresa_id", empresa.getId()).getResultList();
    }
}
