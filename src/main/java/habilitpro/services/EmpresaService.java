package habilitpro.services;

import habilitpro.model.dao.EmpresaDAO;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpresaService {
    private static final Logger LOG = LogManager.getLogger(EmpresaService.class);
    private EntityManager entityManager;
    private EmpresaDAO empresaDAO;

    public EmpresaService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.empresaDAO = new EmpresaDAO(entityManager);
    }


}
