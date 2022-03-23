package habilitpro.model.dao.trilha;

import habilitpro.model.persistence.trilha.Ocupacao;
import habilitpro.model.persistence.trilha.Trilha;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OcupacaoDAO {
    private EntityManager entityManager;

    public OcupacaoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Ocupacao ocupacao) {
        entityManager.persist(ocupacao);
    }

    public void delete(Ocupacao ocupacao) {
        entityManager.remove(ocupacao);
    }

    public Ocupacao update(Ocupacao ocupacao) {
        return entityManager.merge(ocupacao);
    }

    public Ocupacao getById(long id) {
        return entityManager.find(Ocupacao.class, id);
    }

    public Ocupacao findByNome(String nome) {
        String sql = "SELECT * FROM Ocupacao WHERE nome =: nome";
        return (Ocupacao) entityManager.createNativeQuery(sql, Ocupacao.class).setParameter("nome", nome).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Ocupacao> listByTrilha(Trilha trilha) {
        String sql = "SELECT * FROM Ocupacao WHERE trilha =: trilha";
        return entityManager.createNativeQuery(sql, Ocupacao.class).setParameter("trilha", trilha).getResultList();
    }
}
