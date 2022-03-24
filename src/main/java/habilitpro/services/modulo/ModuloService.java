package habilitpro.services.modulo;

import habilitpro.model.dao.modulo.ModuloDAO;
import habilitpro.model.enums.EnumStatusModulo;
import habilitpro.model.persistence.modulo.Modulo;
import habilitpro.model.persistence.trilha.Trilha;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public class ModuloService {
    private static final Logger LOG = LogManager.getLogger(ModuloService.class);
    private EntityManager entityManager;
    private ModuloDAO moduloDAO;

    public ModuloService(EntityManager entityManager, ModuloDAO moduloDAO) {
        this.entityManager = entityManager;
        this.moduloDAO = moduloDAO;
    }

    public void create(Modulo modulo) {
        LOG.info("Preparando para criar um módulo.");

        validateIfNull(modulo);

        String nome = modulo.getNome();
        if (nome == null || nome.isBlank()) {
            LOG.error("O nome do módulo está nulo!");
            throw new RuntimeException("Nome nulo");
        }

        beginTransaction();
        moduloDAO.create(modulo);
        commitAndCloseTransaction();
        LOG.info("Módulo criado com sucesso!");
    }

    public void delete(Long id) {
        LOG.info("Preparando para encontrar o módulo.");

        if (id == null) {
            LOG.error("O ID do módulo está nulo!");
            throw new RuntimeException("ID nulo");
        }

        Modulo modulo = moduloDAO.getById(id);
        validateIfNull(modulo);

        beginTransaction();
        moduloDAO.delete(modulo);
        commitAndCloseTransaction();
        LOG.info("Módulo deletado com sucesso!");
    }

    public void update(Modulo novoModulo, Long id) {
        if (novoModulo == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        Modulo modulo = moduloDAO.getById(id);
        validateIfNull(modulo);
        LOG.info("Módulo encontrado!");

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
    }

    public List<Modulo> listAll() {
        LOG.info("Preparando para listar os módulos.");
        List<Modulo> modulos = moduloDAO.listAll();

        if (modulos == null) {
            LOG.error("Não foram encontrados módulos!");
            throw new RuntimeException("Não há módulos");
        }

        LOG.info("Número de módulos encontrados: " + modulos.size());
        return modulos;
    }

    public List<Modulo> listByStatus(EnumStatusModulo statusModulo) {
        if (statusModulo == null) {
            LOG.error("O status do módulo está nulo!");
            throw new RuntimeException("Status nulo");
        }

        LOG.info("Preparando para listar os módulos com status: " + statusModulo);
        List<Modulo> modulos = moduloDAO.listByStatus(statusModulo);

        if (modulos == null) {
            LOG.error("Não foram encontrados módulos com o status " + statusModulo + "!");
            throw new RuntimeException("Não há módulos com o status informado");
        }

        LOG.info("Número de módulos encontrados: " + modulos.size());
        return modulos;
    }

    public List<Modulo> listByTrilha(Trilha trilha) {
        if (trilha == null) {
            LOG.error("A trilha está nula!");
            throw new RuntimeException("Trilha nula");
        }

        LOG.info("Preparando para listar os módulos da trilha: " + trilha.getNome());
        List<Modulo> modulos = moduloDAO.listByTrilha(trilha);

        if (modulos == null) {
            LOG.error("Não foram encontrados módulos da trilha " + trilha.getNome() + "!");
            throw new RuntimeException("Não há módulos da trilha informada");
        }

        LOG.info("Número de módulos encontrados: " + modulos.size());
        return modulos;
    }

    private void validateIfNull(Modulo modulo) {
        if (modulo == null) {
            this.LOG.error("O módulo não existe!");
            throw new EntityNotFoundException("Módulo nulo");
        }
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
