package habilitpro.services.modulo;

import habilitpro.model.dao.modulo.AvaliacaoModuloDAO;
import habilitpro.model.dao.modulo.ModuloDAO;
import habilitpro.model.enums.EnumStatusModulo;
import habilitpro.model.persistence.modulo.Modulo;
import habilitpro.model.persistence.trilha.Trilha;
import habilitpro.utils.Validar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public class ModuloService {
    private static final Logger LOG = LogManager.getLogger(ModuloService.class);
    private EntityManager entityManager;
    private ModuloDAO moduloDAO;
    private AvaliacaoModuloDAO avaliacaoModuloDAO;

    public ModuloService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.moduloDAO = new ModuloDAO(entityManager);
        this.avaliacaoModuloDAO = new AvaliacaoModuloDAO(entityManager);
    }

    public void create(Modulo modulo) {
        LOG.info("Preparando para criar um módulo.");

        Validar.validarModulo(modulo);

        Validar.validarTrilha(modulo.getTrilha());

        Validar.validarString(modulo.getNome());

        try {
            beginTransaction();
            moduloDAO.create(modulo);
            commitAndCloseTransaction();
            LOG.info("Módulo criado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao criar o módulo, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        LOG.info("Preparando para encontrar o módulo.");

        if (id == null) {
            LOG.error("O ID do módulo está nulo!");
            throw new EntityNotFoundException("ID nulo");
        }

        Modulo modulo = getById(id);

        if (moduloDAO.checkIfAvaliacaoModulo(modulo)) {
            LOG.error("O módulo ainda possui avaliações; delete-as antes de deletar o módulo.");
            throw new RuntimeException("Módulo possui avaliações");
        }

        try {
            beginTransaction();
            moduloDAO.delete(modulo);
            commitAndCloseTransaction();
            LOG.info("Módulo deletado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao deletar o módulo, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(Modulo novoModulo, Long id) {
        if (novoModulo == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new EntityNotFoundException("Parâmetro nulo");
        }

        Modulo modulo = getById(id);

        Validar.validarTrilha(novoModulo.getTrilha());

        Validar.validarString(novoModulo.getNome());

        try {
            beginTransaction();
            modulo.setTrilha(novoModulo.getTrilha());
            modulo.setNome(novoModulo.getNome());
            modulo.setHabilidades(novoModulo.getHabilidades());
            modulo.setTarefa(novoModulo.getTarefa());
            modulo.setDataInicio(novoModulo.getDataInicio());
            modulo.setDataFim(novoModulo.getDataFim());
            modulo.setDataPrazo(novoModulo.getDataPrazo());
            modulo.setPrazoLimite(novoModulo.getPrazoLimite());
            modulo.setStatus(novoModulo.getStatus());
            commitAndCloseTransaction();
            LOG.info("Módulo atualizado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao atualizar o módulo, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Modulo getById(Long id) {
        Validar.validarId(id);

        Modulo modulo = moduloDAO.getById(id);

        Validar.validarModulo(modulo);

        LOG.info("Módulo encontrado!");
        return modulo;
    }

    public List<Modulo> listAll() {
        LOG.info("Preparando para listar os módulos.");
        List<Modulo> modulos = moduloDAO.listAll();

        if (modulos == null) {
            LOG.error("Não foram encontrados módulos!");
            throw new EntityNotFoundException("Não há módulos");
        }

        LOG.info("Número de módulos encontrados: " + modulos.size());
        return modulos;
    }

    public List<Modulo> listByStatus(EnumStatusModulo statusModulo) {
        if (statusModulo == null) {
            LOG.error("O status do módulo está nulo!");
            throw new EntityNotFoundException("Status nulo");
        }

        LOG.info("Preparando para listar os módulos com status: " + statusModulo);
        List<Modulo> modulos = moduloDAO.listByStatus(statusModulo);

        if (modulos == null) {
            LOG.error("Não foram encontrados módulos com o status " + statusModulo + "!");
            throw new EntityNotFoundException("Não há módulos com o status informado");
        }

        LOG.info("Número de módulos encontrados: " + modulos.size());
        return modulos;
    }

    public List<Modulo> listByTrilha(Trilha trilha) {
        Validar.validarTrilha(trilha);

        LOG.info("Preparando para listar os módulos da trilha: " + trilha.getNome());
        List<Modulo> modulos = moduloDAO.listByTrilha(trilha);

        if (modulos == null) {
            LOG.error("Não foram encontrados módulos da trilha " + trilha.getNome() + "!");
            throw new EntityNotFoundException("Não há módulos da trilha informada");
        }

        LOG.info("Número de módulos encontrados: " + modulos.size());
        return modulos;
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
