package habilitpro.model.dao.empresa;

import habilitpro.enums.EnumTipoEmpresa;
import habilitpro.model.persistence.empresa.Empresa;
import habilitpro.model.persistence.empresa.Setor;
import habilitpro.model.persistence.trabalhador.Trabalhador;
import habilitpro.model.persistence.trilha.Trilha;

import javax.persistence.EntityManager;

import java.util.List;

public class EmpresaDAO {

    private EntityManager entityManager;

    public EmpresaDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Empresa empresa) {
        entityManager.persist(empresa);
    }

    public void delete(Empresa empresa) {
        entityManager.remove(empresa);
    }

    // update será implementado na camada de serviços
    public Empresa update(Empresa empresa) {
        return entityManager.merge(empresa);
    }

    public Empresa getById(long id) {
        return entityManager.find(Empresa.class, id);
    }

    public boolean checkIfSetor(Empresa empresa) {
        String sql = "SELECT * FROM Setor WHERE empresa_id =:empresa_id";
        return (entityManager.createNativeQuery(sql, Setor.class).setParameter("empresa_id", empresa.getId()).getResultList().size() > 0);
    }

    public boolean checkIfTrabalhador(Empresa empresa) {
        String sql = "SELECT * FROM Trabalhador WHERE empresa_id =:empresa_id";
        return (entityManager.createNativeQuery(sql, Trabalhador.class).setParameter("empresa_id", empresa.getId()).getResultList().size() > 0);
    }

    public boolean checkIfTrilha(Empresa empresa) {
        String sql = "SELECT * FROM Trilha WHERE empresa_id =:empresa_id";
        return (entityManager.createNativeQuery(sql, Trilha.class).setParameter("empresa_id", empresa.getId()).getResultList().size() > 0);
    }

    @SuppressWarnings("unchecked")
    public List<Empresa> listAll() {
        String sql = "SELECT * FROM Empresa";
        return entityManager.createNativeQuery(sql, Empresa.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Empresa> listByTipo(EnumTipoEmpresa tipoEmpresa) {
        String sql = "SELECT * FROM Empresa WHERE tipoEmpresa =:tipoEmpresa";
        return entityManager.createNativeQuery(sql, Empresa.class).setParameter("tipoEmpresa", tipoEmpresa.getNome()).getResultList();
    }
}
