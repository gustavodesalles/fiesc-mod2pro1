package habilitpro.services.trilha;

import habilitpro.model.dao.trilha.TrilhaDAO;
import habilitpro.model.persistence.empresa.Empresa;
import habilitpro.model.persistence.trilha.Ocupacao;
import habilitpro.model.persistence.trilha.Trilha;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

public class TrilhaService {
    private static final Logger LOG = LogManager.getLogger(TrilhaService.class);
    private EntityManager entityManager;
    private TrilhaDAO trilhaDAO;
    private OcupacaoService ocupacaoService;

    public TrilhaService(EntityManager entityManager, TrilhaDAO trilhaDAO, OcupacaoService ocupacaoService) {
        this.entityManager = entityManager;
        this.trilhaDAO = trilhaDAO;
        this.ocupacaoService = ocupacaoService;
    }

    public void create(Trilha trilha) {
        LOG.info("Preparando para criar a trilha.");

        validateIfNull(trilha);

        Empresa empresa = trilha.getEmpresa();
        String ocupacaoNome = trilha.getOcupacao().getNome();
        LOG.info("Buscando ocupação.");
        Ocupacao ocupacao = ocupacaoService.findByNome(ocupacaoNome);
        if (ocupacao != null) {
            trilha.setOcupacao(ocupacao);
        }

        if (trilha.getNivelSatisfacao() > 5 || trilha.getNivelSatisfacao() < 1) {
            LOG.error("O nível de satisfação não é válido!");
            throw new RuntimeException("Nível de satisfação inválido");
        }

        try {
            trilha.setNome(criarNome(empresa, ocupacao));
            trilha.setApelido(criarApelido(empresa, ocupacao));

            beginTransaction();
            trilhaDAO.create(trilha);
            commitAndCloseTransaction();
        } catch (Exception e) {
            LOG.error("Erro ao criar a trilha, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
        LOG.info("Trilha criada com sucesso!");
    }

    public void delete(Long id) {
        if (id == null) {
            LOG.error("O ID da trilha está nulo!");
            throw new RuntimeException("ID nulo");
        }

        LOG.info("Preparando para encontrar a trilha.");

        Trilha trilha = trilhaDAO.getById(id);
        validateIfNull(trilha);
        LOG.info("Trilha encontrada!");

        beginTransaction();
        trilhaDAO.delete(trilha);
        commitAndCloseTransaction();
        LOG.info("Trilha deletada com sucesso!");
    }

    public void update(Trilha novaTrilha, Long id) {
        if (novaTrilha == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        LOG.info("Preparando para encontrar a trilha.");
        Trilha trilha = trilhaDAO.getById(id);
        validateIfNull(trilha);
        LOG.info("Trilha encontrada!");

        beginTransaction();
        trilha.setEmpresa(novaTrilha.getEmpresa());
        trilha.setOcupacao(novaTrilha.getOcupacao());
        trilha.setNome(criarNome(novaTrilha.getEmpresa(), novaTrilha.getOcupacao()));
        trilha.setApelido(criarApelido(novaTrilha.getEmpresa(), novaTrilha.getOcupacao()));
        trilha.setNivelSatisfacao(novaTrilha.getNivelSatisfacao());
        trilha.setAnotacoes(novaTrilha.getAnotacoes());
        trilha.setTrabalhadores(novaTrilha.getTrabalhadores());
        commitAndCloseTransaction();
        LOG.info("Trilha atualizada com sucesso!");
    }

    public Trilha getById(Long id) {
        if (id == null) {
            LOG.error("O ID da trilha está nulo!");
            throw new RuntimeException("ID nulo");
        }

        Trilha trilha = trilhaDAO.getById(id);

        if (trilha == null) {
            LOG.error("Trilha não encontrada!");
            throw new RuntimeException("Trilha nula");
        }

        LOG.info("Trilha encontrada!");
        return trilha;
    }

    public List<Trilha> listAll() {
        LOG.info("Preparando para listar as trilhas.");
        List<Trilha> trilhas = trilhaDAO.listAll();

        if (trilhas == null) {
            LOG.error("Não foram encontradas trilhas!");
            throw new RuntimeException("Não há trilhas");
        }

        LOG.info("Número de trilhas encontradas: " + trilhas.size());
        return trilhas;
    }

    public List<Trilha> listByEmpresa(Empresa empresa) {
        if (empresa == null) {
            LOG.error("A empresa está nula!");
            throw new RuntimeException("Empresa nula");
        }

        LOG.info("Preparando para buscar as trilhas da empresa: " + empresa.getNome());
        List<Trilha> trilhas = trilhaDAO.listByEmpresa(empresa);

        if (trilhas == null) {
            LOG.error("Não foram encontradas trilhas da empresa " + empresa.getNome() + "!");
            throw new RuntimeException("Não há trilhas da empresa");
        }

        LOG.info("Número de trilhas encontradas: " + trilhas.size());
        return trilhas;
    }

    private void validateIfNull(Trilha trilha) {
        if (trilha == null) {
            LOG.error("A trilha não existe!");
            throw new RuntimeException("Trilha nula");
        }
    }

    private String criarNome(Empresa empresa, Ocupacao ocupacao) {
        int numSeq = getNumSeq(empresa, ocupacao);
        if (!empresa.getNomeFilial().isBlank()) return ocupacao.getNome() + " " + empresa.getNome() + " " + empresa.getNomeFilial() + " " + numSeq + " " + LocalDate.now().getYear();
        return ocupacao.getNome() + " " + empresa.getNome() + " " + numSeq + " " + LocalDate.now().getYear();
    }

    private String criarApelido(Empresa empresa, Ocupacao ocupacao) {
        int numSeq = getNumSeq(empresa, ocupacao);
        return ocupacao.getNome() + " " + numSeq;
    }

    private int getNumSeq(Empresa empresa, Ocupacao ocupacao) {
        int numSeq = 1;
        List<Trilha> trilhas = trilhaDAO.listByEmpresa(empresa);

        for (Trilha t : trilhas) {
            if (t.getOcupacao().equals(ocupacao)) {
                numSeq++;
            }
        }
        return numSeq;
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
