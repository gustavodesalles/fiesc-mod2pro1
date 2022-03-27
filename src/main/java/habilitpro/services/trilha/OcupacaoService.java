package habilitpro.services.trilha;

import habilitpro.model.dao.trilha.OcupacaoDAO;
import habilitpro.model.persistence.trilha.Ocupacao;
import habilitpro.model.persistence.trilha.Trilha;
import habilitpro.utils.Validar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public class OcupacaoService {
    private static final Logger LOG = LogManager.getLogger(OcupacaoService.class);
    private EntityManager entityManager;
    private OcupacaoDAO ocupacaoDAO;

    public OcupacaoService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.ocupacaoDAO = new OcupacaoDAO(entityManager);
    }

    public void create(Ocupacao ocupacao) {
        Validar.validarOcupacao(ocupacao);

        String nome = ocupacao.getNome();
        Validar.validarString(nome);

        try {
            beginTransaction();
            ocupacaoDAO.create(ocupacao);
            commitAndCloseTransaction();
            LOG.info("Ocupação criada com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao criar a ocupação, causado por: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        Validar.validarId(id);

        Ocupacao ocupacao = getById(id);

        if (ocupacaoDAO.checkIfTrilha(ocupacao)) {
            LOG.error("A ocupação ainda possui trilhas; por favor, delete-as antes de deletar a ocupação.");
            throw new RuntimeException("Ocupação possui trilhas");
        }

        try {
            beginTransaction();
            ocupacaoDAO.delete(ocupacao);
            commitAndCloseTransaction();
            LOG.info("Ocupação deletada com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao deletar a ocupação, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(String novoNome, Long id) {
        if (novoNome == null || novoNome.isBlank() || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        LOG.info("Preparando para encontrar a ocupação.");
        Ocupacao ocupacao = getById(id);

        try {
            beginTransaction();
            ocupacao.setNome(novoNome);
            commitAndCloseTransaction();
            LOG.info("Ocupação atualizada com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao atualizar a ocupação, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Ocupacao getById(Long id) {
        Validar.validarId(id);

        Ocupacao ocupacao = ocupacaoDAO.getById(id);
        Validar.validarOcupacao(ocupacao);

        LOG.info("Ocupação encontrada!");
        return ocupacao;
    }

    public Ocupacao findByNome(String nome) {
        Validar.validarString(nome);

        Ocupacao ocupacao = ocupacaoDAO.findByNome(nome);
        Validar.validarOcupacao(ocupacao);

        LOG.info("Ocupação encontrada!");
        return ocupacao;
    }

    public List<Ocupacao> listAll() {
        LOG.info("Preparando para listar as ocupações.");
        List<Ocupacao> ocupacoes = ocupacaoDAO.listAll();

        if (ocupacoes == null) {
            LOG.error("Não foram encontradas ocupações!");
            throw new EntityNotFoundException("Não há ocupações");
        }

        LOG.info("Número de ocupações encontradas: " + ocupacoes.size());
        return ocupacoes;
    }

    public List<Ocupacao> listByTrilha(Trilha trilha) {
        Validar.validarTrilha(trilha);

        LOG.info("Preparando para listar as ocupações da trilha: " + trilha.getNome());
        List<Ocupacao> ocupacoes = ocupacaoDAO.listByTrilha(trilha);

        if (ocupacoes == null) {
            LOG.error("Não foram encontradas ocupações da trilha " + trilha.getNome() + "!");
            throw new EntityNotFoundException("Não há ocupações da trilha informada");
        }

        LOG.info("Número de ocupações encontradas: " + ocupacoes.size());
        return ocupacoes;
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
