package habilitpro.model.dao.empresa;

import habilitpro.model.enums.EnumTipoEmpresa;
import habilitpro.model.persistence.empresa.Empresa;
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

    @SuppressWarnings("unchecked")
    public List<Empresa> listAll() {
        String sql = "SELECT * FROM Empresa";
        return entityManager.createNativeQuery(sql, Empresa.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Empresa> listByTipo(EnumTipoEmpresa tipoEmpresa) {
        String sql = "SELECT * FROM Empresa WHERE tipoEmpresa =: tipoEmpresa";
        return entityManager.createNativeQuery(sql, Empresa.class).setParameter("tipoEmpresa", tipoEmpresa).getResultList();
    }
}
