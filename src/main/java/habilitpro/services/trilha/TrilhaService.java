package habilitpro.services.trilha;

import habilitpro.model.dao.trilha.TrilhaDAO;
import habilitpro.model.persistence.empresa.Empresa;
import habilitpro.model.persistence.trabalhador.Trabalhador;
import habilitpro.model.persistence.trilha.Ocupacao;
import habilitpro.model.persistence.trilha.Trilha;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import habilitpro.services.trabalhador.TrabalhadorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

public class TrilhaService {
    private static final Logger LOG = LogManager.getLogger(TrilhaService.class);
    private EntityManager entityManager;
    private TrilhaDAO trilhaDAO;
    private OcupacaoService ocupacaoService;
    private TrabalhadorService trabalhadorService;

    public TrilhaService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.trilhaDAO = new TrilhaDAO(entityManager);
        this.ocupacaoService = new OcupacaoService(entityManager);
        this.trabalhadorService = new TrabalhadorService(entityManager);
    }

    public void create(Trilha trilha) {
        LOG.info("Preparando para criar a trilha.");

        validateIfNull(trilha);

        Empresa empresa = trilha.getEmpresa();
        validarEmpresa(empresa);

        Ocupacao ocupacao = trilha.getOcupacao();
        if (validarOcupacao(ocupacao) != null) {
            trilha.setOcupacao(validarOcupacao(ocupacao));
        }

        int satisfacao = trilha.getNivelSatisfacao();
        validarSatisfacao(satisfacao);

        try {
            trilha.setNome(criarNome(empresa, ocupacao).toUpperCase());
            trilha.setApelido(criarApelido(empresa, ocupacao).toUpperCase());

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
        validarId(id);

        LOG.info("Preparando para encontrar a trilha.");

        Trilha trilha = getById(id);
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
            throw new EntityNotFoundException("Parâmetro nulo");
        }

        LOG.info("Preparando para encontrar a trilha.");
        Trilha trilha = getById(id);
        validateIfNull(trilha);

        validarEmpresa(novaTrilha.getEmpresa());
        validarOcupacao(novaTrilha.getOcupacao());
        validarSatisfacao(novaTrilha.getNivelSatisfacao());

        try {
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
        } catch (Exception e) {
            LOG.error("Erro ao atualizar a trilha, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Trilha getById(Long id) {
        validarId(id);

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
        validarEmpresa(empresa);

        LOG.info("Preparando para buscar as trilhas da empresa: " + empresa.getNome());
        List<Trilha> trilhas = trilhaDAO.listByEmpresa(empresa);

        if (trilhas == null) {
            LOG.error("Não foram encontradas trilhas da empresa " + empresa.getNome() + "!");
            throw new RuntimeException("Não há trilhas da empresa");
        }

        LOG.info("Número de trilhas encontradas: " + trilhas.size());
        return trilhas;
    }

    public void addTrabalhador(Trilha trilha, Trabalhador trabalhador) {
        validateIfNull(trilha);

        validarTrabalhador(trabalhador);

        try {
            beginTransaction();
            trilha.getTrabalhadores().add(trabalhador);
            trabalhador.getTrilhas().add(trilha);
            commitAndCloseTransaction();
        } catch (Exception e) {
            LOG.error("Erro ao adicionar o trabalhador, causado por: " + e.getMessage());
            throw new RuntimeException(e);
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
            if (t.getOcupacao().getNome().equals(ocupacao.getNome())) {
                numSeq++;
            }
        }
        return numSeq;
    }

    private void validateIfNull(Trilha trilha) {
        if (trilha == null) {
            LOG.error("A trilha não existe!");
            throw new RuntimeException("Trilha nula");
        }
    }

    private void validarEmpresa(Empresa empresa) {
        if (empresa == null) {
            LOG.error("A empresa não existe!");
            throw new EntityNotFoundException("Empresa nula");
        }
    }

    private void validarSatisfacao(int satisfacao) {
        if (satisfacao > 5 || satisfacao < 1) {
            LOG.error("O nível de satisfação não é válido!");
            throw new RuntimeException("Nível de satisfação inválido");
        }
    }

    private Ocupacao validarOcupacao(Ocupacao ocupacao) {
        if (ocupacao == null) {
            LOG.error("A ocupação está nula!");
            throw new EntityNotFoundException("Ocupação nula");
        }

        String ocupacaoNome = ocupacao.getNome();
        LOG.info("Buscando ocupação.");
        return ocupacaoService.findByNome(ocupacaoNome);
    }

    private void validarId(Long id) {
        if (id == null) {
            LOG.error("O ID da trilha está nulo!");
            throw new RuntimeException("ID nulo");
        }
    }

    private void validarTrabalhador(Trabalhador trabalhador) {
        if (trabalhador == null) {
            LOG.error("O trabalhador não existe!");
            throw new EntityNotFoundException("Trabalhador nulo");
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
