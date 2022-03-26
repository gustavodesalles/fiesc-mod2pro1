package habilitpro.model.dao.trabalhador;

import habilitpro.model.persistence.modulo.AvaliacaoModulo;
import habilitpro.model.persistence.trabalhador.Funcao;
import habilitpro.model.persistence.trabalhador.Trabalhador;
import habilitpro.model.persistence.trilha.Trilha;

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

    public boolean checkIfAvaliacaoModulo(Trabalhador trabalhador) {
        String sql = "SELECT * FROM AvaliacaoModulo WHERE trabalhador_id =:trabalhador_id";
        return (entityManager.createNativeQuery(sql, AvaliacaoModulo.class).setParameter("trabalhador_id", trabalhador.getId()).getResultList().size() > 0);
    }

    @SuppressWarnings("unchecked")
    public List<Trabalhador> listAll() {
        String sql = "SELECT * FROM Trabalhador";
        return entityManager.createNativeQuery(sql, Trabalhador.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Trabalhador> listByFuncao(Funcao funcao) {
        String sql = "SELECT * FROM Trabalhador WHERE funcao_id =" + funcao.getId();
        return entityManager.createNativeQuery(sql, Trabalhador.class).setParameter("funcao_id", funcao.getId()).getResultList();
    }
}
