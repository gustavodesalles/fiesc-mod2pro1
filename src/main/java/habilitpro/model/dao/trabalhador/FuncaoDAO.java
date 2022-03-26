package habilitpro.model.dao.trabalhador;

import habilitpro.model.persistence.trabalhador.Funcao;
import habilitpro.model.persistence.empresa.Setor;
import habilitpro.model.persistence.trabalhador.Trabalhador;

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

    public Funcao update(Funcao funcao) {
        return entityManager.merge(funcao);
    }

    public Funcao getById(long id) {
        return entityManager.find(Funcao.class, id);
    }

    public Funcao findByName(String name) {
        String sql = "SELECT * FROM Funcao WHERE name =:name";
        return (Funcao) this.entityManager.createNativeQuery(sql, Funcao.class).setParameter("name",name).getSingleResult();
    }

    public boolean checkIfTrabalhador(Funcao funcao) {
        String sql = "SELECT * FROM Trabalhador WHERE funcao_id =:funcao_id";
        return (entityManager.createNativeQuery(sql, Trabalhador.class).setParameter("funcao_id", funcao.getId()).getResultList().size() > 0);
    }

    @SuppressWarnings("unchecked")
    public List<Funcao> listAll() {
        String sql = "SELECT * FROM Funcao";
        return entityManager.createNativeQuery(sql, Funcao.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Funcao> listBySetor(Setor setor) {
        String sql = "SELECT * FROM Funcao WHERE setor_id = " + setor.getId();
        return entityManager.createNativeQuery(sql, Funcao.class).setParameter("setor_id", setor.getId()).getResultList();
    }
}
