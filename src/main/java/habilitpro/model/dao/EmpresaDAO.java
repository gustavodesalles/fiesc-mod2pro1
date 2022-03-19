package habilitpro.model.dao;

import habilitpro.model.enums.EnumTipoEmpresa;
import habilitpro.model.persistence.Empresa;
import jakarta.persistence.EntityManager;

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

    public List listAll() {
        String sql = "SELECT * FROM Empresa";
        return entityManager.createNativeQuery(sql, Empresa.class).getResultList();
    }

    public List listByTipo(EnumTipoEmpresa tipoEmpresa) {
        String sql = "SELECT * FROM Empresa WHERE tipoEmpresa =: tipoEmpresa";
        return entityManager.createNativeQuery(sql, Empresa.class).setParameter("tipoEmpresa", tipoEmpresa).getResultList();
    }
}
