package habilitpro.services.trilha;

import habilitpro.model.dao.trilha.OcupacaoDAO;
import habilitpro.model.persistence.trilha.Ocupacao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;

public class OcupacaoService {
    private static final Logger LOG = LogManager.getLogger(OcupacaoService.class);
    private EntityManager entityManager;
    private OcupacaoDAO ocupacaoDAO;

    public OcupacaoService(EntityManager entityManager, OcupacaoDAO ocupacaoDAO) {
        this.entityManager = entityManager;
        this.ocupacaoDAO = ocupacaoDAO;
    }

    public Ocupacao getById(Long id) {
        if (id == null) {
            LOG.error("O ID da ocupação está nulo!");
            throw new RuntimeException("ID nulo");
        }

        Ocupacao ocupacao = ocupacaoDAO.getById(id);

        if (ocupacao == null) {
            LOG.error("Ocupação não encontrada!");
            throw new RuntimeException("Ocupação nula");
        }

        return ocupacao;
    }
}
